package com.davila.controller;

import java.io.File;
import java.io.FileInputStream;

import com.davila.service.HashService;
import com.davila.service.PdfEditorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.davila.cache.CacheManager;
import com.davila.exception.FileException;
import com.davila.exception.PDFException;
import com.davila.exception.VerificationException;
import com.davila.model.DocumentReport;
import com.davila.model.DocumentText;
import com.davila.model.DownloadLink;
import com.davila.model.EditedDocument;
import com.davila.model.ImageAddRequest;
import com.davila.model.TextAddRequest;
import com.davila.model.VerificationReport;
import com.davila.parsing.ImageAddRequestParser;
import com.davila.parsing.TextAddRequestParser;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin
@RestController("pdfController")
public class PDFController {
	
	private static final Logger logger = LoggerFactory.getLogger(PDFController.class);
	
	private static String DISKCACHE_DIR = "disk-cache";
	
	@Autowired
	PdfEditorService pdfEditorService;
	
	@Autowired
	CacheManager cacheManager;
	
	@PostMapping(value = "/edit-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Edit a PDF document", description = "This service can add an image and/or text, and set new passwords for encryption in the document. The edited document will be returned in the response as a Base64 String.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "PDF edited successfully"),
	    @ApiResponse(responseCode = "400", description = "Invalid or missing parameter in the request"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public String editPDF(@RequestParam("pdfDocument") MultipartFile pdfDocument,
			@RequestParam(value = "openPassword", required = false) String openPassword, 
			@RequestParam(value = "lockPassword", required = false) String lockPassword,
			@RequestParam(value = "imageToAdd", required = false) MultipartFile imageToAdd,
			@RequestParam(value = "imageAddRequest", required = false) String imageAddRequest,
			@RequestParam(value = "textAddRequest", required = false) String textAddRequest,
			HttpServletRequest request)
			throws PDFException, FileException {
		EditedDocument result = null;
		
		long now = System.currentTimeMillis();
		String path = DISKCACHE_DIR + File.separator;
		
		logger.info("Incoming document editing request");
		
		File diskFolder = CacheManager.getTargetDiskFolder(path);

		File inputFile = processDocumentFile(pdfDocument, diskFolder);
		// Processing the Image File, if present
		File inputImage = processImageFile(imageToAdd, diskFolder, imageAddRequest);
		// Processing the Image Request JSON, if it is present in the request
		ImageAddRequest imgRequest = processImageRequest(imageAddRequest);
		TextAddRequest textRequest = processTextRequest(textAddRequest);
		validatePasswords(openPassword, lockPassword);
		result = pdfEditorService.editPdfDocument(inputFile, openPassword, lockPassword, inputImage, imgRequest, textRequest);

		logger.info("Document editing process sucessfully finished");
		
		long afterEnd = System.currentTimeMillis() - now;
		logger.info("Finished processing document editing request in {} milliseconds.", afterEnd);
		return result.getEditedDocument();
	}
	
	@PostMapping(value = "/transcript-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Transcript a PDF document", description = "This service will get all lines of text in the document, separated by page.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "PDF transcripted successfully"),
	    @ApiResponse(responseCode = "400", description = "Invalid or missing parameter in the request"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public List<DocumentText> transcriptPDF(@RequestParam("pdfDocument") MultipartFile pdfDocument,
			@RequestParam(value = "openPassword", required = false) String openPassword,
			HttpServletRequest request)
			throws PDFException, FileException {
		long now = System.currentTimeMillis();
		
		String fileName = "transcript_pdf_doc_" + pdfDocument.getOriginalFilename();
		String path = DISKCACHE_DIR + File.separator;
		
		logger.info("Incoming transcript PDF document request");
		
		File diskFolder = CacheManager.getTargetDiskFolder(path);
		
		File inputFile = processDocumentFileWithFileName(pdfDocument, diskFolder, fileName);
		List<DocumentText> textsList = null;
		
		try {
			textsList = pdfEditorService.transcriptPdfDocument(inputFile, openPassword);
		} catch (IOException e) {
			throw new PDFException("Exception occured while processing transcript report for PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		long afterEnd = System.currentTimeMillis() - now;
		logger.info("Finished processing transcript report for PDF document request in {} milliseconds.", afterEnd);
		return textsList;
	
	}
	
	@PostMapping(value = "/merge-pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Merge PDF documents", description = "This service can merge two PDFs, by adding all of the Document 1's pages after the last page of Document 2. The merged document will be returned in the response as a Base64 String.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "PDFs merged successfully"),
	    @ApiResponse(responseCode = "400", description = "Invalid or missing parameter in the request"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public EditedDocument mergePDFs(@RequestParam("pdfDocument") MultipartFile pdfDocument,
			@RequestParam(value = "openPassword", required = false) String openPassword,
			@RequestParam("pdfDocument2") MultipartFile pdfDocument2,
			@RequestParam(value = "openPassword2", required = false) String openPassword2,
			HttpServletRequest request)
			throws PDFException, FileException {
		long now = System.currentTimeMillis();
		
		String fileName = "to_merge_pdf_doc_";
		String path = DISKCACHE_DIR + File.separator;
		
		logger.info("Incoming merge PDF documents request");
		
		File diskFolder = CacheManager.getTargetDiskFolder(path);
		
		File inputFile = processDocumentFileWithFileName(pdfDocument, diskFolder, fileName + pdfDocument.getOriginalFilename());
		File inputFile2 = processDocumentFileWithFileName(pdfDocument2, diskFolder, fileName + pdfDocument2.getOriginalFilename());
		validatePasswords(openPassword, null);
		validatePasswords(openPassword2, null);
		File mergedPdf = null;
		try {
			mergedPdf = pdfEditorService.mergePdfDocuments(inputFile, openPassword, inputFile2, openPassword2);
		} catch (IOException e) {
			throw new PDFException("Exception occured while processing merging of PDF Documents: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
	    // Read the file into a byte array
        byte[] fileBytes = null;
		try {
			fileBytes = Files.readAllBytes(Paths.get(mergedPdf.toURI()));
		} catch (IOException e) {
			throw new PDFException("Exception occured while reading merged PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
        // Encode the byte array to a Base64 string
        String base64Merged = Base64.getEncoder().encodeToString(fileBytes);
        // Generating the hash identifier
        byte[] rawPdfDocHash = HashService.generateSHA256Hash(base64Merged.getBytes());
        // Converting the hash to string for use as id
        String pdfDocHashIdentifier = Hex.getString(rawPdfDocHash);
        EditedDocument mergedDoc = new EditedDocument(pdfDocHashIdentifier, base64Merged);

		long afterEnd = System.currentTimeMillis() - now;
		logger.info("Finished processing PDF document merge request in {} milliseconds.", afterEnd);
		return mergedDoc;
	
	}
	
	@PostMapping(value = "/parse-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Parse a PDF document", description = "This service will parse the document (and decrypt it if locked with provided password) and create a report on the features (version, author name, number of signatures, number of pages, etc.).")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "PDF parsed successfully"),
	    @ApiResponse(responseCode = "400", description = "Invalid or missing parameter in the request"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public DocumentReport parsePDF(@RequestParam("pdfDocument") MultipartFile pdfDocument,
			@RequestParam(value = "openPassword", required = false) String openPassword,
			HttpServletRequest request)
			throws PDFException, FileException {
		long now = System.currentTimeMillis();
		
		String fileName = "parse_pdf_doc_" + pdfDocument.getOriginalFilename();
		String path = DISKCACHE_DIR + File.separator;
		
		logger.info("Incoming Parse PDF document request");
		
		File diskFolder = CacheManager.getTargetDiskFolder(path);
		
		File inputFile = processDocumentFileWithFileName(pdfDocument, diskFolder, fileName);
		DocumentReport docReport = null;
		
		docReport = pdfEditorService.readPdfDocument(inputFile, openPassword);

		long afterEnd = System.currentTimeMillis() - now;
		logger.info("Finished processing parsing report for PDF document request in {} milliseconds.", afterEnd);
		return docReport;
	
	}
	
	public File processDocumentFile (MultipartFile pdfDocument, File diskFolder) {
		String fileName = "edit_pdf_doc_" + pdfDocument.getOriginalFilename();
		byte[] fileHashCode = HashService.generateSHA256Hash((fileName).getBytes());
		File inputFile = null;
		try {
			inputFile = cacheManager.saveOrGetFileInDisk(pdfDocument.getInputStream(),
					Hex.getString(fileHashCode), diskFolder);
		} catch (FileException e) {
			throw e;
		} catch (IOException e) {
			throw new PDFException("Exception occured while saving temporary file for PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return inputFile;
	}
	
	public File processDocumentFileWithFileName (MultipartFile pdfDocument, File diskFolder, String fileName) {
		byte[] fileHashCode = HashService.generateSHA256Hash((fileName).getBytes());
		File inputFile = null;
		try {
			inputFile = cacheManager.saveOrGetFileInDisk(pdfDocument.getInputStream(),
					Hex.getString(fileHashCode), diskFolder);
		} catch (FileException e) {
			throw e;
		} catch (IOException e) {
			throw new PDFException("Exception occured while saving temporary file for PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return inputFile;
	}
	
	private ImageAddRequest processImageRequest(String imageAddRequest) {
		ImageAddRequest imgRequest = null;
		if(imageAddRequest != null && !imageAddRequest.isEmpty()) {
			try {
				imgRequest = ImageAddRequestParser.fromJson(imageAddRequest);
				
				//Now let's validate all attributes
				if (imgRequest.getX() < 1) {
					throw new PDFException("The X coordinate for insertion of image is missing or invalid: " + imgRequest.getX(),
							HttpStatus.BAD_REQUEST);
				}
				if (imgRequest.getY() < 1) {
					throw new PDFException("The Y coordinate for insertion of image is missing or invalid: " + imgRequest.getY(),
							HttpStatus.BAD_REQUEST);
				}
				if (imgRequest.getHeight() < 1) {
					throw new PDFException("The height for insertion of image is missing or invalid: " + imgRequest.getHeight(),
							HttpStatus.BAD_REQUEST);
				}
				if (imgRequest.getWidth() < 1) {
					throw new PDFException("The width for insertion of image is missing or invalid: " + imgRequest.getWidth(),
							HttpStatus.BAD_REQUEST);
				}
				if (imgRequest.isAllPages() == true && imgRequest.getPage() > 0) {
					throw new PDFException("The request may only inform a specific page or `allPages` as true for image insertion at a time",
							HttpStatus.BAD_REQUEST);
				} else if(imgRequest.isAllPages() == false && imgRequest.getPage() < 1) {
					throw new PDFException("The page for insertion of image is missing or invalid: " + imgRequest.getPage(),
							HttpStatus.BAD_REQUEST);
				}
				return imgRequest;
			} catch (IOException e) {
				throw new PDFException("Exception while reading the image addition request JSON: " + e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		}
		return imgRequest;
	
	}
	
	private TextAddRequest processTextRequest(String textAddRequest) {
		TextAddRequest txtRequest = null;
		if(textAddRequest != null && !textAddRequest.isEmpty()) {
			try {
				txtRequest = TextAddRequestParser.fromJson(textAddRequest);
				
				//Now let's validate all attributes
				if (txtRequest.getX() < 1) {
					throw new PDFException("The X coordinate for insertion of text in PDF is missing or invalid: " + txtRequest.getX(),
							HttpStatus.BAD_REQUEST);
				}
				if (txtRequest.getY() < 1) {
					throw new PDFException("The Y coordinate for insertion of text in PDF is missing or invalid: " + txtRequest.getY(),
							HttpStatus.BAD_REQUEST);
				}
				if (txtRequest.getText() == null || txtRequest.getText().isEmpty()) {
					throw new PDFException("The text for insertion of text in PDF is missing",
							HttpStatus.BAD_REQUEST);
				}
				if (txtRequest.isAllPages() == true && txtRequest.getPage() > 0) {
					throw new PDFException("The request may only inform a specific page or `allPages` for text insertion as true at a time",
							HttpStatus.BAD_REQUEST);
				} else if(txtRequest.isAllPages() == false && txtRequest.getPage() < 1) {
					throw new PDFException("The page for insertion of text in PDF is missing or invalid: " + txtRequest.getPage(),
							HttpStatus.BAD_REQUEST);
				}
			} catch (IOException e) {
				throw new PDFException("Exception while reading the text addition request JSON: " + e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		}
		return txtRequest;
	}
	
	private File processImageFile(MultipartFile imageToAdd, File diskFolder, String imageRequest) {
		File inputImage = null;
		// Check if imageToAdd is null or empty
		if (imageToAdd != null && !imageToAdd.isEmpty()) {
			try {
				String imageFileName = "edit_pdf_image_" + imageToAdd.getOriginalFilename();
				byte[] imageFileHashCode = HashService.generateSHA256Hash((imageFileName).getBytes());
				String fileFormat[] = imageFileName.split("\\.");
				inputImage = cacheManager.saveOrGetFileInDisk(imageToAdd.getInputStream(),
						Hex.getString(imageFileHashCode)+"."+fileFormat[1], diskFolder);
			} catch (FileException e) {
				throw e;
			} catch (IOException e) {
				throw new FileException("Exception occured while saving temporary file for image: " + e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		} else if(imageRequest != null && !imageRequest.isEmpty()) {
			//Has image addition request but not the image in the request
			throw new PDFException("Parameter `imageToAdd` must be present in the request if a image addition object is also present.",
					HttpStatus.BAD_REQUEST);
		}
		return inputImage;
		
	}
	
	
	private void validatePasswords (String openPassword, String lockPassword) {
		if(openPassword != null) {
			if(openPassword.isEmpty()) {
				throw new PDFException("The informed openPassword is empty or invalid (has invalid characters).",
						HttpStatus.BAD_REQUEST);
			}
			
		}
		if(lockPassword != null) {
			if(lockPassword.isEmpty() || lockPassword.length() > 128) {
				throw new PDFException("The informed lockPassword is empty or invalid (beyond 128 characters or has invalid characters).",
						HttpStatus.BAD_REQUEST);
			}
		}
	}
	
}

package com.davila.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.encryption.ProtectionPolicy;
import org.apache.pdfbox.pdmodel.encryption.SecurityHandler;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.encryption.StandardSecurityHandler;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDListBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.davila.cache.CacheManager;
import com.davila.controller.PDFController;
import com.davila.exception.PDFException;
import com.davila.model.EditedDocument;
import com.davila.model.ImageAddRequest;
import com.davila.model.TextAddRequest;
import com.davila.model.DocumentReport;
import com.davila.model.DocumentText;

@Service("pdfEditorService")
public class PdfEditorService {
	
	private static final Logger logger = LoggerFactory.getLogger(PdfEditorService.class);
	
	@Autowired
	CacheManager cacheManager;

	public EditedDocument editPdfDocument(File inputFile, String openPassword, String lockPassword, File inputImage, ImageAddRequest imageRequest, TextAddRequest textRequest) {
		PDDocument pdDocument = null;
		try {
			pdDocument = readPdfDocumentObjectFromDisk(inputFile.getAbsolutePath(), openPassword);
		} catch (IOException e) {
			throw new PDFException("Exception occured during PDF document parsing: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		// Process the addition of image to document
		if(imageRequest != null && inputImage != null) {
			try {
				addImageToPDF(pdDocument, inputImage, imageRequest);
			} catch (IOException e) {
				throw new PDFException("Exception occured during writing of image in PDF Document: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
		// Process the addition of text to document
		if(textRequest != null) {
			try {
				addTextToPDF(pdDocument, textRequest);
			} catch (IOException e) {
				throw new PDFException("Exception occured during writing of text in PDF Document: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
		
		// Apply encryption if applicable
		applyProtectionPolicy(pdDocument, lockPassword, openPassword);
	
		// Saving the PDF Document
		File savedPdfDoc = savePdfDocument(inputFile, pdDocument);
		byte[] docBytes = this.getDocByteArray(savedPdfDoc);
        String editedPdfDocument = Base64.getEncoder().encodeToString(docBytes);
        
        // The edited PDF document identifier in the cache will be the hash of its content
        byte[] rawPdfDocHash = HashService.generateSHA256Hash(editedPdfDocument.getBytes());
        String pdfDocHashIdentifier = Hex.getString(rawPdfDocHash);
		
        // Finished creating the edited PDF object
		EditedDocument finishedDocument = new EditedDocument(pdfDocHashIdentifier, editedPdfDocument);
		try {
			pdDocument.close();
		} catch (IOException e) {
			throw new PDFException("Unexpected exception occured.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PDAnnotation annotation;
		
		return finishedDocument;
	}

	public DocumentReport readPdfDocument(File inputFile, String openPassword) {
		PDDocument pdDocument = null;
		try {
			pdDocument = readPdfDocumentObjectFromDisk(inputFile.getAbsolutePath(), openPassword);
		} catch (IOException e) {
			throw new PDFException("Exception occured during PDF document parsing: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		PDDocumentInformation docInfo = pdDocument.getDocumentInformation();
		
		DocumentReport docReport = new DocumentReport();
		
		//General info about pdf document
		docReport.setAuthor(docInfo.getAuthor()); 
		docReport.setSubject(docInfo.getSubject()); 
		docReport.setCreationDate(docInfo.getCreationDate().toInstant().toString());
		docReport.setCreator(docInfo.getCreator());
		docReport.setProducer(docInfo.getProducer());
		docReport.setNumberOfPages(pdDocument.getNumberOfPages()); 
		docReport.setPdfVersion(""+pdDocument.getVersion()); 
		docReport.setNumberOfSignatures(pdDocument.getSignatureFields().size());
		docReport.setHasEncryption(pdDocument.getEncryption() != null);
		
		//Access Permissions, if applicable
		if(pdDocument.getCurrentAccessPermission() != null) {
			AccessPermission permissions = pdDocument.getCurrentAccessPermission();
			docReport.setCanAssembleDocument(permissions.canAssembleDocument());
			docReport.setCanExtractContent(permissions.canExtractContent());
			docReport.setCanExtractForAccessibility(permissions.canExtractForAccessibility());
			docReport.setCanFillInForm(permissions.canFillInForm());
			docReport.setCanModifyAnnotations(permissions.canModifyAnnotations());
			docReport.setCanPrint(permissions.canPrint());
			docReport.setCanPrintFaithful(permissions.canPrintFaithful());
		}
		
		return docReport;
	}
	
	public File mergePdfDocuments(File file, String openPassword, File file2, String openPassword2) throws IOException {
		File mergedDoc = null;
		// Create PDFMergerUtility instance
		PDFMergerUtility pdfMerger = new PDFMergerUtility();

		PDDocument pdDocument1 = null;
		try {
			pdDocument1 = readPdfDocumentObjectFromDisk(file.getAbsolutePath(), openPassword);
		} catch (IOException e) {
			throw new PDFException("Exception occured during PDF document parsing: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		PDDocument pdDocument2 = null;
		try {
			pdDocument2 = readPdfDocumentObjectFromDisk(file2.getAbsolutePath(), openPassword2);
		} catch (IOException e) {
			throw new PDFException("Exception occured during PDF document parsing: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		// Set the destination file
		mergedDoc = new File(file.getParent(), "merged_doc_" + file.getName());
		pdfMerger.setDestinationFileName(mergedDoc.getAbsolutePath());
			
		try {
			// Merge the PDFs
			pdfMerger.appendDocument(pdDocument1, pdDocument2);
			logger.info("File with FileID {} added in the local disk cache.", mergedDoc.getName());
			logger.info("PDFs merged successfully!");
		} catch (IOException e) {
			throw new PDFException("Exception occured merging of the PDF Documents: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		return mergedDoc;
	}
	
	public List<DocumentText> transcriptPdfDocument(File file, String openPassword) throws IOException {
        List<DocumentText> documentTexts = new ArrayList<>();

        try (PDDocument document = readPdfDocumentObjectFromDisk(file.getAbsolutePath(), openPassword)) {
            PDFTextStripper stripper = new PDFTextStripper();
            int numberOfPages = document.getNumberOfPages();

            for (int i = 0; i < numberOfPages; i++) {
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String pageText = stripper.getText(document);
                String[] lines = pageText.split("\r\n|\r|\n");

                for (int j = 0; j < lines.length; j++) {
                	if(lines[j] != null && !lines[j].isEmpty())
                		documentTexts.add(new DocumentText(i + 1, j + 1, lines[j]));
                }
            }
        }

        return documentTexts;
    }

	/**
	 * Add an image to an existing PDF document.
	 *
	 * @param pdDocument The input PDF to add the image to.
	 * @param image      The filename of the image to put in the PDF.
	 * @param outputFile The file to write to the pdf to.
	 *
	 * @throws IOException If there is an error writing the data.
	 */
	public void addImageToPDF(PDDocument pdDocument, File image, ImageAddRequest imageRequest) throws IOException {
		try {
			PDImageXObject ximage = PDImageXObject.createFromFile(image.getAbsolutePath(), pdDocument);
			logger.info("Processing image addition to PDF Document...");
			if (imageRequest.isAllPages()) {
				// Will add the image to all of the PDF's pages
				PDPageTree allPages = (PDPageTree) pdDocument.getDocumentCatalog().getPages();
				for (int i = 0; i < allPages.getCount(); i++) {
					PDPage page = (PDPage) allPages.get(i);

					PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, AppendMode.APPEND,
							false, true);

					contentStream.drawImage(ximage, imageRequest.getX(), imageRequest.getY(), imageRequest.getWidth(),
							imageRequest.getHeight());

					contentStream.close();
				}
				logger.info("Images added sucessfully to PDF Document");
			} else {
				// Will add the image to the defined page.
				PDPage page = null;
				PDPageTree allPages = (PDPageTree) pdDocument.getDocumentCatalog().getPages();
				try {
					page = (PDPage) allPages.get(imageRequest.getPage()-1); // -1 because it adds 1 inside get
				} catch (IndexOutOfBoundsException e) {
					throw new PDFException("The page for insertion of image is missing or invalid: " + imageRequest.getPage(),
							HttpStatus.BAD_REQUEST);
				}

				PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, AppendMode.APPEND, false,
						true);

				contentStream.drawImage(ximage, imageRequest.getX(), imageRequest.getY(), imageRequest.getWidth(),
						imageRequest.getHeight());
				
				logger.info("Image added sucessfully to the page of the PDF Document");

				contentStream.close();
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Add text to an existing PDF document.
	 *
	 * @param pdDocument The input PDF to add the text to.
	 * @param text       The text to add to the PDF.
	 * @param textRequest The details of where and how to add the text.
	 *
	 * @throws IOException If there is an error writing the data.
	 */
	public void addTextToPDF(PDDocument pdDocument, TextAddRequest textRequest) throws IOException {
	    try {
	        logger.info("Processing text addition to PDF Document...");
	        if (textRequest.isAllPages()) {
	            // Will add the text to all of the PDF's pages
	            PDPageTree allPages = (PDPageTree) pdDocument.getDocumentCatalog().getPages();
	            for (int i = 0; i < allPages.getCount(); i++) {
	                PDPage page = (PDPage) allPages.get(i);

	                try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, AppendMode.APPEND, true, true)) {
	                    contentStream.beginText();
	                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), textRequest.getFontSize());
	                    contentStream.newLineAtOffset(textRequest.getX(), textRequest.getY());
	                    contentStream.showText(textRequest.getText());
	                    contentStream.endText();
	                }
	            }
	            logger.info("Text added successfully to all pages of the PDF Document");
	        } else {
	            // Will add the text to the defined page.
	            PDPage page = (PDPage) pdDocument.getDocumentCatalog().getPages().get(textRequest.getPage()-1);

	            try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, AppendMode.APPEND, true, true)) {
	                contentStream.beginText();
	                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), textRequest.getFontSize());
	                contentStream.newLineAtOffset(textRequest.getX(), textRequest.getY());
	                contentStream.showText(textRequest.getText());
	                contentStream.endText();
	            }
	            
	            logger.info("Text added successfully to the page of the PDF Document");
	        }
	    } catch (IOException e) {
	        throw e;
	    }
	}

	public PDDocument readPdfDocumentObjectFromDisk(String inputFile, String openPassword) throws IOException {
		PDDocument pdDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(inputFile), openPassword, MemoryUsageSetting.setupTempFileOnly().streamCache);
		logger.info("Finished reading the PDDocument from the PDF File");
		return pdDocument;
	}

	public File savePdfDocument(File inputFile, PDDocument docToBeSaved) {
		File savedDoc = new File(inputFile.getParent(), "saved_doc_" + inputFile.getName());
		try {
			docToBeSaved.save(savedDoc);
			logger.info("Edited document PDF {} saved sucessfully.", savedDoc.getName());
		} catch (IOException e) {
			throw new PDFException("Exception occured during saving of the finished PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}

		return savedDoc;
	}

	public byte[] getDocByteArray(File savedDoc) {
		byte[] docBytes;
		try {
			docBytes = fileToByteArray(savedDoc);
			logger.info("Deleting the already processed PDF document {} from the disk cache.", savedDoc.getName());
			cacheManager.deleteFileFromDisk(savedDoc.getAbsolutePath());
		} catch (IOException e) {
			throw new PDFException("Exception occured during reading of edited PDF Document: " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return docBytes;
	}
	
	public static byte[] fileToByteArray(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
	
	public void applyProtectionPolicy(PDDocument pdDocument, String lockPassword, String openPassword) {
		AccessPermission accessPermission = null;
		
		 // Get the existing encryption settings
		if (pdDocument.getEncryption() != null && openPassword != null && !openPassword.isEmpty()) {
			accessPermission = pdDocument.getCurrentAccessPermission();
		}
		
		if (accessPermission != null) {
			if (lockPassword != null && !lockPassword.isEmpty()) {
				// Create a new protection policy with the existing permissions but a new password
				StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(lockPassword, lockPassword,
						accessPermission);
				protectionPolicy.setEncryptionKeyLength(128);

				// Apply the new protection policy
				try {
					pdDocument.protect(protectionPolicy);
				} catch (IOException e) {
					throw new PDFException(
							"Exception occured during application of encryption in PDF Document: " + e.getMessage(),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				// Create a new protection policy with the existing permissions and the current password
				StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(openPassword, openPassword,
						accessPermission);
				protectionPolicy.setEncryptionKeyLength(128);

				// Apply the new protection policy
				try {
					pdDocument.protect(protectionPolicy);
				} catch (IOException e) {
					throw new PDFException(
							"Exception occured during application of encryption in PDF Document: " + e.getMessage(),
							HttpStatus.BAD_REQUEST);
				}
			}
		} else if(lockPassword != null && !lockPassword.isEmpty()) {
			// Document does not have encryption and new encryption is to be applied
			accessPermission = new AccessPermission();
			accessPermission = generateAccessPermissions(accessPermission);

			StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(lockPassword, lockPassword, accessPermission);
			protectionPolicy.setPermissions(accessPermission);
			protectionPolicy.setEncryptionKeyLength(128);
			protectionPolicy.setPreferAES(true);
			final PDEncryption encryptionDict = new PDEncryption();
			pdDocument.setEncryptionDictionary(encryptionDict);
			// Apply the new protection policy
			try {
				pdDocument.protect(protectionPolicy);
			} catch (IOException e) {
				throw new PDFException(
						"Exception occured during application of encryption in PDF Document: " + e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		} 
		// Will do nothing if doc has no encryption and no lock password is present in the request
	}
	
	public AccessPermission generateAccessPermissions(AccessPermission accessPermission) {
		accessPermission.setCanAssembleDocument(true);
		accessPermission.setCanFillInForm(true);
		accessPermission.setCanModifyAnnotations(true);
		accessPermission.setCanPrint(true);
		accessPermission.setCanPrintFaithful(true);	
		
		//Evaluate permitting the user to set readOnly and if is modifiable
		//accessPermission.setCanModify(false);
		//accessPermission.setCanExtractForAccessibility(false);
		//accessPermission.setCanExtractContent(false);
		//accessPermission.setReadOnly();
		return accessPermission;
	}
	
    public AccessPermission bytesToAccessPermission(byte[] accessPermissionBytes) {
        AccessPermission accessPermission = new AccessPermission();

        // Ensure the byte array has at least one byte for the flags
        if (accessPermissionBytes != null && accessPermissionBytes.length > 0) {
            byte flags = accessPermissionBytes[0];

            // Set permissions based on the byte flags
            accessPermission.setCanPrint((flags & 0x01) != 0); // Print permission
            accessPermission.setCanModify((flags & 0x02) != 0); // Modify permission
            accessPermission.setCanModifyAnnotations((flags & 0x08) != 0); // Annotate permission
            accessPermission.setCanFillInForm((flags & 0x10) != 0); // Fill in forms
            accessPermission.setCanExtractContent((flags & 0x20) != 0); // Extract content
            accessPermission.setCanAssembleDocument((flags & 0x40) != 0); // Assemble document
            accessPermission.setCanPrintFaithful((flags & 0x80) != 0); // High-res print
            accessPermission.setCanExtractForAccessibility(true);
        }

        return accessPermission;
    }
	
	public void reApplyProtectionPolicy(PDDocument pdDocument, String openPassword) {
		AccessPermission accessPermission = null;

		if (pdDocument.getEncryption() != null && openPassword != null && !openPassword.isEmpty()) {
			PDEncryption encryption = pdDocument.getEncryption();

			try {
				accessPermission = new AccessPermission(encryption.getPerms());
			} catch (IOException e) {
				throw new PDFException(
						"Exception occured during reading of encryption dictionary permission in PDF Document: "
								+ e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		}

		if (accessPermission != null) {
			// Create a new protection policy with the existing permissions and the current password
			StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(openPassword, openPassword,
					accessPermission);
			protectionPolicy.setEncryptionKeyLength(128);

			// Apply the new protection policy
			try {
				pdDocument.protect(protectionPolicy);
			} catch (IOException e) {
				throw new PDFException(
						"Exception occured during application of encryption in PDF Document: " + e.getMessage(),
						HttpStatus.BAD_REQUEST);
			}
		}
	}
}

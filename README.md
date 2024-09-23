# Java-PDF-Reader-Editor

A simple Java application that has support for functionalities pertaining PDF edition and parsing. The API can edit PDFs (add images and text, and encryption passwords), merge two PDFs into one, and transcript all text from a PDF document.

This API was made to serve a front-end application that will offer the services from its endpoints in a browser form.

You can access FSDavila's Front-End application for this API here: https://github.com/FSDavila/

Also, it has robust and organized logging (example):

```PDFController : Incoming document editing request```  
```CacheManager : File with FileID bb13b1c395a74a453dcb72a5f4b5d9d571652f0b1a38ffa633c7a435578b4af5 added in the local disk cache.```  
```CacheManager : File with FileID f82dd3a23eec9663540d3f4018ccf0881d9d3cd3f9b2226eb25d4517dd3b7bb9.png added in the local disk cache.```  
```PdfEditorService : Finished reading the PDDocument from the PDF File```  
```PdfEditorService : Processing image addition to PDF Document...```  
```PdfEditorService : Image added sucessfully to PDF Document```  
```PdfEditorService : Processing text addition to PDF Document...```  
```PdfEditorService : Text added successfully to all pages of the PDF Document```  
```PdfEditorService : Edited document PDF saved_doc_bb13b1c395a74a453dcb72a5f4b5d9d571652f0b1a38ffa633... saved sucessfully.```  
```PdfEditorService : Deleting the already processed PDF document saved_doc_bb13b1c395a74a453dcb72a5f4... from the disk cache.```  
```CacheManager     : File with FileID saved_doc_bb13b1c395a74a453dcb72a5f4b5d9d571652f0b1a38ffa633c... deleted from local disk cache.```  
```PDFController    : Document editing process sucessfully finished```  
```FileService      : Edited file stored in Memory cache with key: 0279c6fa72931e401fb8ad318e8c557552f8006c7ab86faee4bf87976916b5bc```  
```PDFController : Finished processing document editing request in 62 milliseconds.```  

A detailed Postman Collection is also provided in the package for easier learning of its usage.

There is also a Swagger documentation page for the services, that can be accessed after booting the application.

Example of PDF Doc edited with the application:
![image](https://github.com/user-attachments/assets/d2c99f32-cfdf-4287-88cb-550a111d297f)

The main endpoints are (all services are compatible with PDFs locked with password):

-POST /edit-pdf: This service can add an image and/or text, and set new passwords for encryption in the document. The edited document will be returned in the response as a Base64 String.  
-POST /transcript-pdf: This service will get all lines of text in the document, separated by page.  
-POST /merge-pdfs: This service can merge two PDFs, by adding all of the Document 1's pages after the last page of Document 2. The merged document will be returned in the response as a Base64 String.  

## Technologies used:
-Java  
-SpringBoot v2.7.0 (For creating the API endpoints)  
-Logback  
-Apache PDFBox v3.0.3 (For editing, merging and transcripting PDFs)  
-Google Guava (for Robust Memory Cache)  
-Rest Assured (for supporting requests in JUnit Tests)  
-JUnit (for automated tests for the API and its behaviors)  
-Disk Caching (temporary files are cached in the disk) 

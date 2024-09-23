package com.davila.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.davila.model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(PDFException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(PDFException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatusCode(), ""+System.currentTimeMillis());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
    
    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatusCode(), ""+System.currentTimeMillis());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
    
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ErrorResponse> handleVerificationException(VerificationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatusCode(), ""+System.currentTimeMillis());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
    
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, ""+System.currentTimeMillis());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ""+System.currentTimeMillis());
    	logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        
    }
}

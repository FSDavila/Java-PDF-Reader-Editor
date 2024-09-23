package com.davila.exception;

import org.springframework.http.HttpStatus;

public class VerificationException extends RuntimeException  {
		private static final long serialVersionUID = 1L;

		private HttpStatus statusCode;
		private String message;
		
		public VerificationException(String message, HttpStatus statusCode) {
			this.setStatusCode(statusCode);
			this.setMessage(message);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public HttpStatus getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(HttpStatus statusCode) {
			this.statusCode = statusCode;
		}
}

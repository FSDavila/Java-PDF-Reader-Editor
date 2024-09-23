package com.davila.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON response
public class ErrorResponse {
    private String message;
    private int statusCode;
    private String path;
    private String timeStamp;

    public ErrorResponse(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode.value();
    }
    
    public ErrorResponse(String message, HttpStatus statusCode, String timeStamp) {
        this.message = message;
        this.statusCode = statusCode.value();
        this.timeStamp = timeStamp;
    }
    
    public ErrorResponse(String message, HttpStatus statusCode, String timeStamp, String path) {
        this.message = message;
        this.statusCode = statusCode.value();
        this.timeStamp = timeStamp;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
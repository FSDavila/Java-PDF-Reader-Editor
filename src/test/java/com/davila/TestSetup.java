package com.davila;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

public class TestSetup {
	static int serverPort = 8080;
	protected static String URL_PDFAPI = "http://localhost";
	private static String baseFilePath;
	
	@BeforeAll
	public static void setupbasePath() throws URISyntaxException {
		RestAssured.baseURI = URL_PDFAPI;
		RestAssured.port = serverPort;
		try {
			baseFilePath = TestSetup.class.getClassLoader().getResource(".").toURI().getPath();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.apache.http");
		root.setLevel(ch.qos.logback.classic.Level.INFO);
		ch.qos.logback.classic.Logger root2 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("httpclient");
		root2.setLevel(ch.qos.logback.classic.Level.INFO);
		ch.qos.logback.classic.Logger root3 = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("io.restassured.internal");
		root3.setLevel(ch.qos.logback.classic.Level.ERROR);
		
	}
	
	public void saveFileToDisk(byte[] fileBytes, String name) throws IOException {
        // Define the path to save the file
        File editedFilesDir = new File("editedFiles");
        if (!editedFilesDir.exists()) {
            editedFilesDir.mkdirs(); // Create directory if it doesn't exist
        }

        // Save the file
        File savedFile = new File(editedFilesDir, name + ".pdf");
        try (OutputStream outputStream = new FileOutputStream(savedFile)) {
            outputStream.write(fileBytes);
        }
	}
}

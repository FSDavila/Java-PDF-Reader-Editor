package com.davila.editpdf;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.davila.TestSetup;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EditPdfTest extends TestSetup {
	
	// Tests covering SUCCESS scenarios
	
	@Test
	public void whenCorrectEditPdfWithImageRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithImageAllPagesRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("allPages", "true");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithTextRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("page", "1");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This a automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithTextAllPagesRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("allPages", "true");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This a automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithTextAndImageAllPagesRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("allPages", "true");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This a automated test.");
		
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "20.0");
		imageRequest.put("y", "10.0");
		imageRequest.put("width", "40.0");
		imageRequest.put("height", "40.0");
		imageRequest.put("allPages", "true");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithTextAndImageDifferentPagesRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("page", "3");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This a automated test.");
		
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "20.0");
		imageRequest.put("y", "10.0");
		imageRequest.put("width", "40.0");
		imageRequest.put("height", "40.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfWithTextDiffPositionRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "70.0");
		textRequest.put("y", "70.0");
		textRequest.put("page", "1");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This a automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditEncryptedPdfWithImageRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3_encrypted.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");
		requestSpecBuilder.addMultiPart("openPassword", "test123", "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditEncryptedPdfToEncryptWithNewPasswordImageRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3_encrypted.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");
		requestSpecBuilder.addMultiPart("openPassword", "test123", "application/json");
		requestSpecBuilder.addMultiPart("lockPassword", "test321", "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	@Test
	public void whenCorrectEditPdfToEncryptWithPasswordImageRequestIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		String functionName = testInfo.getTestMethod().get().getName();
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");
		requestSpecBuilder.addMultiPart("lockPassword", "test321", "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(200, response.getStatusCode());
		assertNotNull(response);

		// Get content as bytes
		byte[] fileBytes = response.asByteArray();
		fileBytes = Base64.getDecoder().decode(fileBytes);
		assertNotNull(fileBytes);
		saveFileToDisk(fileBytes, functionName);
	}
	
	// Tests covering ERROR scenarios
	
	@Test
	public void whenIncorrectEditPdfWrongOpenPasswordIsGivenForEncryptedDoc_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3_encrypted.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("openPassword", "WRONGPASSOHNOOOOOO", "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("Exception occured during PDF document parsing: Cannot decrypt PDF, the password is incorrect", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfInvalidLockPasswordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("lockPassword", "", "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The informed lockPassword is empty or invalid (beyond 128 characters or has invalid characters).", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfTooBigLockPasswordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("lockPassword", "WHOAIAMTOOBIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIG", "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The informed lockPassword is empty or invalid (beyond 128 characters or has invalid characters).", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfInvalidOpenPasswordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("openPassword", "", "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The informed openPassword is empty or invalid (has invalid characters).", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestMissingXCoordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The X coordinate for insertion of image is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestMissingYCoordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The Y coordinate for insertion of image is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestWrongPageIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "5");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The page for insertion of image is missing or invalid: 5", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestMissingPageIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The page for insertion of image is missing or invalid: 0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestMissingWidthIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The width for insertion of image is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestMissingHeightIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The height for insertion of image is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestWithBothSpecificAndAllPagesIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		File image = new File(this.getClass().getResource("/images/pdf-icon.png").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");
		imageRequest.put("allPages", "true");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");
		requestSpecBuilder.addMultiPart("imageToAdd", image, "image/png");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The request may only inform a specific page or `allPages` as true for image insertion at a time", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfTextRequestWithMissingTextIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("page", "1");
		textRequest.put("fontSize", "11");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The text for insertion of text in PDF is missing", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfTextRequestWithMissingXCoordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("y", "10.0");
		textRequest.put("page", "1");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This is an automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The X coordinate for insertion of text in PDF is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfTextRequestWithMissingYCoordIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("page", "1");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This is an automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The Y coordinate for insertion of text in PDF is missing or invalid: 0.0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfTextRequestWithMissingPageIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject textRequest = new JSONObject();
		textRequest.put("x", "10.0");
		textRequest.put("y", "10.0");
		textRequest.put("fontSize", "11");
		textRequest.put("text", "This is an automated test.");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("textAddRequest", textRequest.toString(), "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("The page for insertion of text in PDF is missing or invalid: 0", response.jsonPath().get("message"));
	}
	
	@Test
	public void whenIncorrectEditPdfImageRequestWithNoImageIsGiven_then200(TestInfo testInfo) throws JSONException, IOException {
		File pdfDocument = new File(this.getClass().getResource("/pdfFiles/test3.pdf").getFile());
		assertNotNull(pdfDocument);
		
		JSONObject imageRequest = new JSONObject();
		imageRequest.put("x", "1.0");
		imageRequest.put("y", "1.0");
		imageRequest.put("width", "100.0");
		imageRequest.put("height", "100.0");
		imageRequest.put("page", "1");

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.addMultiPart("pdfDocument", pdfDocument, "application/pdf");
		requestSpecBuilder.addMultiPart("imageAddRequest", imageRequest.toString(), "application/json");

		RequestSpecification requestSpecification = requestSpecBuilder.build();

		Response response = given().spec(requestSpecification).when().post("/edit-pdf").andReturn();

		assertEquals(400, response.getStatusCode());
		assertEquals("Parameter `imageToAdd` must be present in the request if a image addition object is also present.", response.jsonPath().get("message"));
	}
}

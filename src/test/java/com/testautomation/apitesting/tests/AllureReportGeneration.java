package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

@Epic("Epic-01")
@Feature("Create Update Delete Booking")
public class AllureReportGeneration extends BaseTest {

	private static final Logger logger = LogManager.getLogger(AllureReportGeneration.class);

	@Story("Story 1")
	@Test(description = "end to end api testing")
	@Description("end to end testing")
	@Severity(SeverityLevel.CRITICAL)
	public void e2eAPIRequest() {

		logger.info("e2eAPIRequest test execution started...");

		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
					"UTF-8");
			String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
					"UTF-8");
			String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
					"UTF-8");
			String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
					"UTF-8");

			// post api call
			logger.info("Post API test execution started...");
			Response response = RestAssured
					.given()
					.filter(new AllureRestAssured())       // Add Allure filter
	                .filter(new RequestLoggingFilter())    // Log the request details
	                .filter(new ResponseLoggingFilter())   // Log the response details
						.contentType(ContentType.JSON).body(postAPIRequestBody)
						.baseUri(BASE_URI)
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();

			JSONArray jsonArray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String firstName = (String) jsonArray.get(0);

			Assert.assertEquals(firstName, "api testing");

			int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
			System.out.println("Booking Id : " + bookingId);
			logger.info("POST request successful, booking ID: {}", bookingId);
			logger.info("Post API test execution End...");
			
			// get api call
			logger.info("GET::: Testing valid booking ID: {}", bookingId);
			RestAssured
				.given().filter(new AllureRestAssured())
				.filter(new RequestLoggingFilter())    // Log the request details
                .filter(new ResponseLoggingFilter())   // Log the response details
					.contentType(ContentType.JSON)
					.baseUri(BASE_URI)
				.when()
					.get("/{bookingId}", bookingId)
				.then()
					.assertThat()
					.statusCode(200);
			logger.info("GET API test passed for booking ID: {}", bookingId);
			
			
			// token generation
			Response tokenAPIResponse = RestAssured
					.given().filter(new AllureRestAssured())
					.filter(new RequestLoggingFilter())    // Log the request details
	                .filter(new ResponseLoggingFilter())   // Log the response details
						.contentType(ContentType.JSON)
						.body(tokenAPIRequestBody)
						.baseUri("https://restful-booker.herokuapp.com/auth")
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();

			String token = JsonPath.read(tokenAPIResponse.body().asString(), "$.token");
			System.out.println("Token Id : " + token);
			logger.info("Token Generated Successfully: "+token);

			// put api call
			logger.info("Put API test execution started...");
			RestAssured
			.given().filter(new AllureRestAssured())
			.filter(new RequestLoggingFilter())    // Log the request details
            .filter(new ResponseLoggingFilter())   // Log the response details
				.contentType(ContentType.JSON)
				.body(putAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.put("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200).body("firstname", equalTo("Specflow"))
				.body("lastname", equalTo("Selenium C#"));

			 logger.info("PUT request successful, booking ID: {}", bookingId);
			 
			// patch api call
			logger.info("Patch API test execution started...");
			RestAssured
			.given().filter(new AllureRestAssured())
			.filter(new RequestLoggingFilter())    // Log the request details
            .filter(new ResponseLoggingFilter())   // Log the response details
				.contentType(ContentType.JSON)
				.body(patchAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.patch("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", equalTo("Ajay Test"));
			logger.info("Patch request successful, booking ID: {}", bookingId);
			
			// delete api call
			logger.info("Delete API test execution started...");
			RestAssured
			.given().filter(new AllureRestAssured())
			.filter(new RequestLoggingFilter())    // Log the request details
            .filter(new ResponseLoggingFilter())   // Log the response details
				.contentType(ContentType.JSON)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.delete("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(201);
			 logger.info("Delete API test execution Completed...");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("e2eAPIRequest test execution ended...");

	}
	
	@Story("Story 1")
	@Test(description = "e2eAPITest2")
	@Description("end to end testing")
	@Severity(SeverityLevel.BLOCKER)
	public void e2eAPITest2() {

		logger.info("e2eAPIRequest test execution started...");

		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY),
					"UTF-8");
			String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
					"UTF-8");
			String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
					"UTF-8");
			String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
					"UTF-8");

			// post api call
			logger.info("Post API test execution started...");
			Response response = RestAssured
					.given().filter(new AllureRestAssured())
			
						.contentType(ContentType.JSON).body(postAPIRequestBody)
						.baseUri(BASE_URI)
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();

			JSONArray jsonArray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String firstName = (String) jsonArray.get(0);

			Assert.assertEquals(firstName, "api testing");

			int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
			System.out.println("Booking Id : " + bookingId);
			logger.info("POST request successful, booking ID: {}", bookingId);
			logger.info("Post API test execution End...");

			// get api call
			logger.info("GET::: Testing valid booking ID: {}", bookingId);
			RestAssured
				.given().filter(new AllureRestAssured())
				
					.contentType(ContentType.JSON)
					.baseUri(BASE_URI)
				.when()
					.get("/{bookingId}", bookingId)
				.then()
					.assertThat()
					.statusCode(200);
			logger.info("GET API test passed for booking ID: {}", bookingId);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("e2eAPIRequest test execution ended...");

	}


}

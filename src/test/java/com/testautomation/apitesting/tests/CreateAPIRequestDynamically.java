package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.ExtentReportManager;
import com.testautomation.apitesting.utils.FileNameConstants;
import com.testautomation.apitesting.utils.RestAPIHelper;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class CreateAPIRequestDynamically extends BaseTest {

	private ExtentTest test;
	@Test
	public void e2eAPIRequest() {
		test = ExtentReportManager.createTest("Create API Request Dynamically");
		test.info("End to End APIRequest test execution started...");

		try {
			String postAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_API_REQUEST_BODY_DYNAMIC),
					"UTF-8");
			String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_API_REQUEST_BODY),
					"UTF-8");
			String putAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PUT_API_REQUEST_BODY),
					"UTF-8");
			String patchAPIRequestBody = FileUtils.readFileToString(new File(FileNameConstants.PATCH_API_REQUEST_BODY),
					"UTF-8");
			
			test.info("POST:: Creation of booking details:: STARTS ****");
			String dynamicAPIRequest = RestAPIHelper.prepareAPIRequestDynamically(postAPIRequestBody, "postman by","testers talk","chicken");
			// Log the request body to verify
			System.out.println("Request Body: " + dynamicAPIRequest.toString());
			test.info("Request Body: " + dynamicAPIRequest.toString());

			// post api call
			Response response = RestAssured
					.given()
					.filter(new RequestLoggingFilter())
					.filter(new ResponseLoggingFilter())
					.contentType(ContentType.JSON).body(dynamicAPIRequest)
						.baseUri(BASE_URI)
					.when()
						.post()
					.then()
						.assertThat()
						.statusCode(200)
					.extract()
						.response();

			test.pass("Booking created successfully with status code: " + response.getStatusCode());
			JSONArray jsonArray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String firstName = (String) jsonArray.get(0);
			
			Assert.assertEquals(response.getStatusCode(), 200);
			test.pass("POST API Response code is: "+response.getStatusCode());
			Assert.assertEquals(firstName, "postman by");
			test.pass("firstName is: "+firstName);
			int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
			System.out.println("Booking Id : " + bookingId);
			test.pass("Booking Id is: "+bookingId);
			
			// Extract the response body
	        JSONObject responseBody = new JSONObject(response.getBody().asString());
	        test.info("Response Body: " + responseBody.toString());
	        test.info("POST:: Creation of booking details:: ENDS ****");
			// get api call
	        test.info("Get API to get the booking details:: STARTS ****");
	        Response getResponse =RestAssured
				.given()
				.filter(new RequestLoggingFilter())
				.filter(new ResponseLoggingFilter())
					.contentType(ContentType.JSON)
					.baseUri(BASE_URI)
				.when()
					.get("/{bookingId}", bookingId)
				.then()
					.assertThat()
					.statusCode(200)
					.extract()
					.response();
			
	        Assert.assertEquals(getResponse.getStatusCode(), 200);
			test.pass("GET API Response code is: "+getResponse.getStatusCode());
			// Extract the response body
	        JSONObject getresponseBody = new JSONObject(getResponse.getBody().asString());
	        test.info("Response Body: " + getresponseBody.toString());

			test.info("Get API to get the booking details:: ENDS ****");
			
			test.info("GET TOKEN:: Token script execution -> STARTS ****");
			// token generation
			Response tokenAPIResponse = RestAssured
					.given()
					.filter(new RequestLoggingFilter())
					.filter(new ResponseLoggingFilter())
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
			Assert.assertEquals(tokenAPIResponse.getStatusCode(), 200);
			test.pass("GET API Token Response code is: "+tokenAPIResponse.getStatusCode());
			System.out.println("Token Id : " + token);
			test.pass("Generated token is: "+token);
			// Extract the response body
	        JSONObject tokenresponseBody = new JSONObject(tokenAPIResponse.getBody().asString());
	        test.info("Response Body: " + tokenresponseBody.toString());
	        test.info("GET TOKEN:: Token script execution -> ENDS ****");
	        
			// put api call
	        test.info("PUT:: Updation of booking details:: STARTS ****");
	       // Log the request body to verify
	     	System.out.println("Request Body: " + putAPIRequestBody.toString());
	     	test.info("Request Body: " + putAPIRequestBody.toString());
			Response putResponse = RestAssured
			.given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
				.contentType(ContentType.JSON)
				.body(putAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.put("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200).body("firstname", equalTo("Specflow"))
				.body("lastname", equalTo("Selenium C#"))
				.extract()
				.response();

			Assert.assertEquals(putResponse.getStatusCode(), 200);
			test.pass("PUT API Token Response code is: "+putResponse.getStatusCode());
			
			// Extract the response body
	        JSONObject putresponseBody = new JSONObject(putResponse.getBody().asString());
	        test.info("Response Body: " + putresponseBody.toString());
			test.info("PUT:: Updation of booking details:: ENDS ****");
			  
			// patch api call
			test.info("PATCH:: Updation of booking details partially:: STARTS ****");
			// Log the request body to verify
	     	System.out.println("Request Body: " + patchAPIRequestBody.toString());
	     	test.info("Request Body: " + patchAPIRequestBody.toString());
			Response patchResponse =RestAssured
			.given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
				.contentType(ContentType.JSON)
				.body(patchAPIRequestBody)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.patch("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", equalTo("Ajay Test"))
				.extract()
				.response();
			
			Assert.assertEquals(patchResponse.getStatusCode(), 200);
			test.pass("PATCH API Token Response code is: "+patchResponse.getStatusCode());
			
			
			// Extract the response body
			JSONObject patchResponseBody = new JSONObject(patchResponse.getBody().asString());
			test.info("Response Body: "+ patchResponseBody);
			test.info("PATCH:: Updation of booking details partially:: ENDS ****");
			
			// delete api call
			test.info("DELETE:: Deletion of booking details:: STARTS ****");
			Response deleteResponse=RestAssured
			.given()
			.filter(new RequestLoggingFilter())
			.filter(new ResponseLoggingFilter())
				.contentType(ContentType.JSON)
				.header("Cookie", "token=" + token)
				.baseUri(BASE_URI)
			.when()
				.delete("/{bookingId}", bookingId)
			.then()
				.assertThat()
				.statusCode(201)
				.extract()
				.response();
			
			Assert.assertEquals(deleteResponse.getStatusCode(), 201);
			test.pass("DELETE API Token Response code is: "+deleteResponse.getStatusCode());
			
		// Extract the response body
			String deleteresponseBody = deleteResponse.getBody().asString();  // Get the body as a String
			test.info("Response Body: " + deleteresponseBody);  // Log to Extent Report
			test.info("DELETE:: Deletion of booking details:: ENDS ****");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.info("End to End APIRequest test execution ended...");

	}

}

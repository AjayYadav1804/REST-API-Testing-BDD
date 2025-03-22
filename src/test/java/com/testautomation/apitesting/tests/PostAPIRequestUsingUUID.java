package com.testautomation.apitesting.tests;


import java.io.IOException;
import java.util.Random;
import java.util.UUID;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.ExtentReportManager;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;


public class PostAPIRequestUsingUUID extends BaseTest{
	 private ExtentTest test;
	
	@Test
	public void postAPIRequest() throws IOException {
		
		 test = ExtentReportManager.createTest("Create Booking Test");
	
        // Generate unique values
        String firstname = "Jim" + UUID.randomUUID().toString().substring(0, 5);
        String lastname = "Brown" + UUID.randomUUID().toString().substring(0, 5);
        int totalprice = new Random().nextInt(1000) + 1;

        // Request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", firstname);
        requestBody.put("lastname", lastname);
        requestBody.put("totalprice", totalprice);
        requestBody.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2018-01-01");
        bookingDates.put("checkout", "2019-01-01");

        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Breakfast");

		// Log the request body to verify
		System.out.println("Request Body: " + requestBody.toString());
		test.info("Request Body: " + requestBody.toString());

		Response response =
		RestAssured
				.given()
				.filter(new RequestLoggingFilter())
				.filter(new ResponseLoggingFilter())
					.contentType(ContentType.JSON)
					.body(requestBody.toString())
					.baseUri(BASE_URI)
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
				.extract()
					.response();
		
		// Verify the response
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP Response Code: 200");
        test.pass("Booking created successfully with status code: " + response.getStatusCode());
        // Extract the response body
        JSONObject responseBody = new JSONObject(response.getBody().asString());
        test.info("Response Body: " + responseBody.toString());

        // Assertions to verify the response
        Assert.assertNotNull(responseBody.getInt("bookingid"), "Booking ID should not be null");
     // Print the entire response body
        System.out.println("Response Body: " + response.getBody().asString());
        
        JSONObject booking = responseBody.getJSONObject("booking");
        Assert.assertEquals(booking.getString("firstname"), firstname, "Firstname mismatch");
        Assert.assertEquals(booking.getString("lastname"), lastname, "Lastname mismatch");
        Assert.assertEquals(booking.getInt("totalprice"), totalprice, "Total price mismatch");
        Assert.assertTrue(booking.getBoolean("depositpaid"), "Deposit paid mismatch");

        JSONObject responseBookingDates = booking.getJSONObject("bookingdates");
        Assert.assertEquals(responseBookingDates.getString("checkin"), "2018-01-01", "Checkin date mismatch");
        Assert.assertEquals(responseBookingDates.getString("checkout"), "2019-01-01", "Checkout date mismatch");

        Assert.assertEquals(booking.getString("additionalneeds"), "Breakfast", "Additional needs mismatch");
        
        
        // Print the entire response body
        System.out.println("Response Body: " + response.getBody().asString());
        
        String bookingIdstr = response.jsonPath().getString("bookingid");
        int bookingId = Integer.parseInt(bookingIdstr);
		System.out.println("Booking Id : " + bookingId);
 
        
        // Print the entire response body
        System.out.println("Response Body: " + response.getBody().asString());
        
        
RestAssured
			.given()
				.contentType(ContentType.JSON)
				.baseUri(BASE_URI)
			.when()
			.get("/" + bookingId)
			.then()
			.assertThat()
			.statusCode(200);
		
	}

}

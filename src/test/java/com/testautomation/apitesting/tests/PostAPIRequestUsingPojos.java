package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import com.github.javafaker.Faker;
public class PostAPIRequestUsingPojos extends BaseTest {

	@Test
	public void postAPIRequest() {

		try {
			
			String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA),"UTF-8");
			 Faker faker = new Faker();
			 // Use Faker to generate random data
            String firstname = faker.name().firstName();
            String lastname = faker.name().lastName();
            String additionalneeds = faker.food().dish();
            int totalprice = faker.number().numberBetween(100, 1000);
			
			BookingDates bookingDates = new BookingDates("2023-03-25", "2023-03-30");
			Booking booking = new Booking(firstname, lastname, additionalneeds, totalprice, true, bookingDates);

			//serialization
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
			System.out.println(requestBody);

			// de-serialization
			Booking bookingDetails = objectMapper.readValue(requestBody, Booking.class);
			System.out.println(bookingDetails.getFirstname());
			System.out.println(bookingDetails.getTotalprice());
			System.out.println(bookingDetails.getBookingdates().getCheckin());
			System.out.println(bookingDetails.getBookingdates().getCheckout());
			
			Response response =
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(requestBody)
					.baseUri(BASE_URI)
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
				.extract()
					.response();
			
			int bookingId = response.path("bookingid");
			
			//System.out.println(jsonSchema);
			
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.baseUri(BASE_URI)
				.when()
					.get("/{bookingId}",bookingId)
				.then()
					.assertThat()
					.statusCode(200)
					.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}

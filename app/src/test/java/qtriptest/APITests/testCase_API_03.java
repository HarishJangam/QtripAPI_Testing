package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Random;

public class testCase_API_03 {

    @Test(description = "Verify registration and make booking", groups={"API Tests"})
    public void testCase03() {

        // Hit the register API
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/register";
        // String url="https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/register"
        RequestSpecification registerRequest = RestAssured.given();
        registerRequest.contentType(ContentType.JSON);

        // defining dynamic email id for multiple registrations
        Random random = new Random();
        String s=String.valueOf(System.currentTimeMillis());
        int randomNumber = random.nextInt(100);
        String dynamicEmail = "QA_API" + randomNumber + "@gmail.com";

        HashMap<Object, Object> registerPayload = new HashMap<>();
        registerPayload.put("email", dynamicEmail);
        registerPayload.put("password", "testtest");
        registerPayload.put("confirmpassword", "testtest");

        registerRequest.body(registerPayload);
        Response registerResponse = registerRequest.request(Method.POST);
        // Response response=registerRequest.POST()
        // Validate registration response
        registerResponse.getStatusCode();
        registerResponse.then().assertThat().statusCode(201);

        // Hit login API and validate
        String loginEndPointURL = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/login";

        RequestSpecification loginRequest = RestAssured.given();
        loginRequest.contentType(ContentType.JSON);

        // Make payload for hitting login API
        registerPayload.remove("confirmpassword");

        loginRequest.body(registerPayload);
        Response loginResponse = loginRequest.post(loginEndPointURL);
        System.out.println(loginResponse.getBody().asPrettyString());
        loginResponse.then().assertThat().statusCode(201);

        JsonPath loginJsonPath = loginResponse.jsonPath();
        boolean loginStatusValue = loginJsonPath.get("success");
        Assert.assertTrue(loginStatusValue);

        String token = loginJsonPath.get("data.token");
        String id = loginJsonPath.get("data.id");

        // System.out.println(token);
        // System.out.println(id);

        // Prepare headers for booking API with the new OAuth token
        Headers headers = new Headers(new Header("Authorization", "Bearer " + token));

        // Create a new map for booking payload
        HashMap<Object, Object> bookingPayload = new HashMap<>();
        bookingPayload.put("userId", id);
        bookingPayload.put("name", "testuser");
        bookingPayload.put("date", "2024-02-09");
        bookingPayload.put("person", "12");
        bookingPayload.put("adventure", "2447910730");

        // Create a new RequestSpecification with headers
        RequestSpecification bookingRequestSpec = RestAssured.given().headers("Authorization", "Bearer ",token);
        bookingRequestSpec.contentType(ContentType.JSON);

        // Add the request payload to the RequestSpecification
        bookingRequestSpec.body(bookingPayload);

        // Define the booking API endpoint URL
        String bookingApiEndpoint = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/reservations/new";

        // Perform the POST request to the booking API
        Response bookingResponse = bookingRequestSpec.post(bookingApiEndpoint);

        // Print booking response
        System.out.println("Booking Response: " + bookingResponse.getBody().asPrettyString());

        // Assertions and additional checks can be added based on the booking response
        boolean bookingStatusValue = bookingResponse.jsonPath().get("success");
        Assert.assertTrue(bookingStatusValue);

        // booking history API endpoint URL
        String bookingHistoryApiEndpoint = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/reservations/?id="+id;
        
        // Create a new RequestSpecification with headers booking history calling
        RequestSpecification bookingHistoryRequestSpec = RestAssured.given().headers(headers);
        bookingHistoryRequestSpec.contentType(ContentType.JSON);

        // booking history response
        Response bookingHistoryReponse = bookingHistoryRequestSpec.get(bookingHistoryApiEndpoint);

        System.out.println(bookingHistoryReponse.getBody().asPrettyString());

        // Assertions and additional checks can be added based on the booking response
        String userId = bookingHistoryReponse.jsonPath().get("[0].userId");
        String adventure = bookingHistoryReponse.jsonPath().get("[0].adventure");

        Assert.assertEquals(userId, id, "UserId not succesfully validated");
        Assert.assertEquals(adventure, bookingPayload.get("adventure"), "Adventure not successfully validated");

    }
}

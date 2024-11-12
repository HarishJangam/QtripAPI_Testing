// package qtriptest.APITests;

// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import io.restassured.path.json.JsonPath;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;
// import org.json.JSONObject;
// import org.testng.Assert;
// import org.testng.annotations.Test;
// import java.util.HashMap;
// import java.util.UUID;

// public class testCase_API_04 {

//     @Test(description="API for negative testing of register.",groups="API Tests")
//     // @Test(priority = 4,description = "re registation functionality tesing",groups = {"API test"})
//     public void testCase04(){
//         String url="https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/register";
//         RequestSpecification httpResquest=RestAssured.given();
//         httpResquest.contentType(ContentType.JSON);
//         UUID uuid=UUID.randomUUID();
//         String dynamicEmail="harish"+uuid+"@gmail.com";
//         HashMap<String,String>mp=new HashMap<>();
//         mp.put("email",dynamicEmail);
//         mp.put("password","harish");
//         mp.put("confirmpassword","harish");
//         httpResquest.body(mp);
//         Response response=httpResquest.post(url);
//         response.then().assertThat().statusCode(201);
//         System.out.println(response.getBody());
//         JsonPath jsonpath=response.jsonPath();    

//         response=httpResquest.post(url);

//         response.then().assertThat().statusCode(400);
//     }
// }

package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Random;

public class testCase_API_04 {

    @Test(description= "Verify registration and login", groups={"API Tests"})
    public void testCase04() {
        
    //Hit the register api
    RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net"; 
    RestAssured.basePath="/api/v1/register";

    RequestSpecification httpRequest = RestAssured.given(); 
    httpRequest.contentType (ContentType.JSON);

    //defining dynamic email id for multiple registration 
    Random random = new Random();
    int randomNumber = random.nextInt(100);
    String dynamicEmail = "QA_API"+randomNumber+"@gmail.com";

    HashMap<Object, Object> map= new HashMap<Object, Object>(); 
    map.put("email", dynamicEmail);
    map.put("password", "testtest"); 
    map.put("confirmpassword", "testtest");

    httpRequest.body(map);
    Response response = httpRequest.request(Method.POST);

    // second registration with same userId
    // httpRequest.body(map);
    Response response2 = httpRequest.request(Method.POST);

    // int actualStatusCode
    response2.getStatusCode();
    response2.then().assertThat().statusCode (400);

    System.out.println(response2.getBody().asPrettyString());

    JsonPath jsonpath = response2.jsonPath();
    String message = jsonpath.get("message");
    Assert.assertEquals(message, "Email already exists", "Duplicated registration not validated");

    } 

}

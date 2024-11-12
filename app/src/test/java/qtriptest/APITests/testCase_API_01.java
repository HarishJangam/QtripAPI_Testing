package qtriptest.APITests;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;



public class testCase_API_01 {
    
    @Test (description="API for register and login",groups="API Tests")
    // @Test(priority = 1 ,groups = {"API test"},description = "User Registration and login verification")
   public void testCase01(){
    RestAssured.baseURI="https://content-qtripdynamic-qa-backend.azurewebsites.net";
    RestAssured.basePath="/api/v1/register";
    RequestSpecification httpRequest=RestAssured.given();
    httpRequest.contentType(ContentType.JSON);
    // Random rand=new Random();
    // int randomNumber=rand.nextInt(100);
    String timestamp=String.valueOf(System.currentTimeMillis());
    UUID uuid=UUID.randomUUID();
    String dynamicEmail="harish"+uuid+"@gmail.com";
    HashMap<Object,Object>mp=new HashMap<>();
    mp.put("email",dynamicEmail);
    mp.put("password","Harish");
    mp.put("confirmpassword", "Harish");
    httpRequest.body(mp);
    Response response=httpRequest.post();
    System.out.println(response.getBody().asPrettyString());
    response.then().assertThat().statusCode(201);
    String loginPath="https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/login";
    RequestSpecification httpRequest2=RestAssured.given();
    mp.remove("confirmpassword");
    httpRequest2.contentType(ContentType.JSON);
    httpRequest2.body(mp);
    Response response2=httpRequest2.post(loginPath);
    response2.then().assertThat().statusCode(201);
    JsonPath jsonpath=response2.jsonPath();
    boolean statusValue=jsonpath.get("success");
    String str=jsonpath.get("data.token");
    System.out.println(str);
    System.out.println(response2.getBody().asPrettyString());
    Assert.assertTrue(statusValue,"something went Wrong");

   }
}

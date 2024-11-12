package qtriptest.APITests;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

public class testCase_API_02 {

    @Test(description= "Verify cities", groups={"API Tests"})
    public void testCase02() {
        
    //Hit the register api
    RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net"; 
    // RestAssured.basePath="/api/v1/cities?q=beng";
    RestAssured.basePath="/api/v1/cities?q=beng";


    RequestSpecification httpRequest = RestAssured.given(); 
    httpRequest.contentType(ContentType.JSON);

    Response response = httpRequest.get(RestAssured.baseURI+RestAssured.basePath);

    // int actualStatusCode
    response.getStatusCode();
    response.then().assertThat().statusCode(200);

    System.out.println(response.getBody().asPrettyString());

    // Check the length of the array
    int arrayLength = response.jsonPath().getList("$").size();
    Assert.assertEquals(arrayLength, 1, "Array length is not 1");

    // Check the value of the "description" field
    String descriptionValue = response.jsonPath().getString("[0].description");
    Assert.assertEquals(descriptionValue, "100+ Places", "Description value is incorrect");

    } 
}

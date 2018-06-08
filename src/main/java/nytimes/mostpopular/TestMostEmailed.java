package nytimes.mostpopular;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Akhmad on 08.06.2018.
 */
public class TestMostEmailed extends TestMostPopularBase {

    @Test
    public void TestGetMostEmailedArticles(){
        given()
                .spec(reqSpecMostPopular)
                .when()
                .get(config.getProperty("mostemailed"))
                .then()
                .log().all(true)
                .statusCode(200)
                .body("status", response -> equalTo("OK"))
                .body("num_results", response -> notNullValue());
    }
}

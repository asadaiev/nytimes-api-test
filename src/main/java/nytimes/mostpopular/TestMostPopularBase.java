package nytimes.mostpopular;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nytimes.common.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Akhmad on 08.06.2018.
 */
public class TestMostPopularBase {

    Configuration config =  new Configuration("/config.properties");

    protected final RequestSpecification reqSpecMostPopular =
            new RequestSpecBuilder().setBaseUri(config.getProperty("mostpopular_url"))
                    .setContentType(ContentType.JSON)
                    .addHeader("api-key", config.getProperty("api-key"))
                    .build().log().all(true);


//    @BeforeClass
//    public void beforeClass(){
//        config = new Configuration("/config.properties");
//    }
    @Test
    public void getAlltest(){
        System.out.println(getAllSections());
    }

    protected Set getAllSections() {

        ValidatableResponse section_response = getEntity(config.getProperty("all_section_url"))
                .then()
                .statusCode(200)
                .body("status", response -> equalTo("OK"))
                .body("num_results", response -> notNullValue());

//        System.out.println("HERE RESPONSE ===========>>>>>>>>" + section_response.extract().response().path("results[0].name"));
        Set<String> sections = new LinkedHashSet();
        for (int i = 0; i < (Integer) section_response.extract().response().path("num_results"); i++){
            sections.add(section_response.extract().response().path("results[" + i + "].name"));
        }

        System.out.println(sections);
        return sections;
    }


    private Response getEntity(String prop) {
        return given()
                .spec(reqSpecMostPopular)
                .header("api-key", config.getProperty("api-key"))
                .when()
                .get(prop);
    }
}
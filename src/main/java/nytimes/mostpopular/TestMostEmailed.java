package nytimes.mostpopular;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Akhmad on 08.06.2018.
 */
public class TestMostEmailed extends TestMostPopularBase {

    Map<String, Integer> out;
    private int numberOfMostEmailed;

    @Test
    public void TestGetMostEmailedArticles() {
        ValidatableResponse respAll = getEntity(config.getProperty("mostemailed"))
                .then()
                .log().all(true)
                .statusCode(200)
                .body("status", response -> equalTo("OK"))
                .body("num_results", response -> notNullValue());

        JsonPath jsonPath = new JsonPath(respAll.extract().response().body().asString());
        numberOfMostEmailed = jsonPath.getInt("num_results");


    }
//    //
//    @Test
    public void calculateEachSectionInGetAllRequest() {
        out = new LinkedHashMap<>(numberOfMostEmailed);
        for (int j = 0; j < numberOfMostEmailed; j += 20) {
            ValidatableResponse getAllRequest = getEntity(config.getProperty("mostemailed") + "?offset=" + j)
                    .then()
                    .log().all(true)
                    .statusCode(200)
                    .body("status", response -> equalTo("OK"))
                    .body("num_results", response -> notNullValue());


            for (int i = 0; i < 20; i++) {

                if (out.containsKey(getPath(getAllRequest, "results[" + i + "].section"))) {
                    out.put(getPath(getAllRequest, "results[" + i + "].section"),
                            out.get(getPath(getAllRequest, "results[" + i + "].section")) + 1);
                    continue;
                }
                out.put(getPath(getAllRequest, "results[" + i + "].section"), 1);
            }
        }

        if(out.containsKey(null)){
            out.remove(null);
        }
        for (String str : out.keySet()) {
            System.out.print(str + " = " + out.get(str) + " | ");
        }
    }


    //Also we could you here DataProvider
    @Test
    public void testCompareSections(String section){

        calculateEachSectionInGetAllRequest();
        getEntity("/mostemailed/" + section + "/30.json")
                .then()
                .log().all(true)
                .statusCode(200)
                .body("status", response -> equalTo("OK"))
                .body("num_results", response -> equalTo(out.get(section)));
    }
}

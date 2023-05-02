import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

public class Lesson_2_4_headers {

    @Test
    public void testRestAssuredHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1","myValue1");
        headers.put("myHeader2","myValue2");

        Response response= RestAssured
                .given()
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();

        response.prettyPrint();

        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
    }

    @Test
    public void testRestAssuredHeaderLocation() {

        Response response= RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }
}

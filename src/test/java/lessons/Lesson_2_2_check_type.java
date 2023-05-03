package lessons;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class Lesson_2_2_check_type {

    @Test
    public void testRestAssuredGet() {
        Response response= RestAssured
                .given()
                .queryParam("param1","value1")
                .queryParam("param2","value2")
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        //response.prettyPrint();
        response.print();
    }

    @Test
    public void testRestAssuredPost() {
        Response response= RestAssured
                .given()
                .body("param1=value1&param2=value2")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        //response.prettyPrint();
        response.print();
    }

    @Test
    public void testRestAssuredPostJson() {
        Response response= RestAssured
                .given()
                .body("{\"param1\":\"value1\",\"param2\":\"value2\"}")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        //response.prettyPrint();
        response.print();
    }

    @Test
    public void testRestAssuredPostMap() {
        Map<String,Object> body = new HashMap<>();
        body.put("param1","value1");
        body.put("param2","value2");

        Response response= RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        //response.prettyPrint();
        response.print();
    }
}

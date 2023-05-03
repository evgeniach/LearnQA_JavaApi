package lessons;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Lesson_3_1_junit {

    @Test
    public void testRestAssuredAssertTrue() {
        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertTrue(response.statusCode()==200,"Unexpected status code");
    }

    @Test
    public void testRestAssuredAssertEqualsFor200() {
        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200,response.statusCode(),"Unexpected status code");
    }

    @Test
    public void testRestAssuredAssertEqualsFor404() {
        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/mape")
                .andReturn();
        assertEquals(404,response.statusCode(),"Unexpected status code");
    }
}

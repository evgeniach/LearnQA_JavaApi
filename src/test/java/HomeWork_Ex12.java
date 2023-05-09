import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork_Ex12 {

    @Test
    public void testAuthUser() {

        Response responseGetAuth = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers headers = responseGetAuth.getHeaders();

        assertTrue(headers.hasHeaderWithName("x-secret-homework-header"),"Response doesn't have 'x-secret-homework-header' header");
        assertTrue(headers.getValue("x-secret-homework-header").equals("Some secret value"),"Response header is not equal to 'Some secret value'");

    }
}

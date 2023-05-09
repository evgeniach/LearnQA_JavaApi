import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork_Ex11 {

    @Test
    public void testAuthUser() {

        Response responseGetAuth = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();

        assertTrue(cookies.containsKey("HomeWork"), "Response doesn't have 'HomeWork' cookie");

    }
}

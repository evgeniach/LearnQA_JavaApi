package lessons;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson_2_5_cookie {

    @Test
    public void testRestAssuredCookies() {
        Map<String, String> data = new HashMap<>();
        data.put("login","secret_login");
        data.put("password","secret_pass");

        Response response= RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeader = response.getHeaders();
        System.out.println(responseHeader);

        System.out.println("\nCookies:");
        Map<String,String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
    }

    @Test
    public void testRestAssuredCookieAuthCookie() {
        Map<String, String> data = new HashMap<>();
        data.put("login","secret_login");
        data.put("password","secret_pass");

        Response response= RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = response.getCookie("auth_cookie");
        System.out.println(responseCookie);
    }

    @Test
    public void testRestAssuredCookieAuthCookieError() {
        Map<String, String> data = new HashMap<>();
        data.put("login","secret_login2");
        data.put("password","secret_pass2");

        Response response= RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = response.getCookie("auth_cookie");
        System.out.println(responseCookie);
    }

    @Test
    public void testRestAssuredCookiesError() {
        Map<String, String> data = new HashMap<>();
        data.put("login","secret_login2");
        data.put("password","secret_pass2");

        Response response= RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeader = response.getHeaders();
        System.out.println(responseHeader);

        System.out.println("\nCookies:");
        Map<String,String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
    }


}

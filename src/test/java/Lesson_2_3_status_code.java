import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Lesson_2_3_status_code {

    @Test
    public void testRestAssured200() {

        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }

    @Test
    public void testRestAssured500() {

        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/get_500")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }

    @Test
    public void testRestAssured404() {

        Response response= RestAssured
                .get("https://playground.learnqa.ru/api/something")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }

    @Test
    public void testRestAssured303() {

        Response response= RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }

}

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HomeWork_Ex7 {

    @Test
    public void Ex7() {

        int statusCode = 0;
        String response_url="https://playground.learnqa.ru/api/long_redirect";
        int countsRedirects = 0;

        while(statusCode!=200) {

            Response response= RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(response_url)
                    .andReturn();

            statusCode = response.getStatusCode();

            if (statusCode==301) {
                countsRedirects++;
                response_url = response.getHeader("Location");
                System.out.println("Location for redirect: "+response_url);
            }
        }
        System.out.println("Count of redirects: "+countsRedirects);
    }

}

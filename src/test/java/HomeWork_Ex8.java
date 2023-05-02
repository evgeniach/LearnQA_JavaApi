import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class HomeWork_Ex8 {

    @Test
    public void Ex8() throws InterruptedException {

        String response_url="https://playground.learnqa.ru/api/longtime_job";

        JsonPath response = RestAssured
                .get(response_url)
                .jsonPath();

        String token = response.get("token");
        int sleepTime = response.get("seconds");
        sleepTime = 1000*sleepTime;

        JsonPath responseSecond= RestAssured
                .given()
                .queryParam("token",token)
                .when()
                .get(response_url)
                .jsonPath();

        String status = responseSecond.get("status");

        if (status.equals("Job is NOT ready")) {
            Thread.sleep(sleepTime);

            JsonPath responsetThird= RestAssured
                    .given()
                    .queryParam("token",token)
                    .when()
                    .get(response_url)
                    .jsonPath();

            status = responsetThird.get("status");
            String result = responsetThird.get("result");

            if (status.equals("Job is ready") && result!=null) {
                    System.out.println("Status is right and result is exist.");
            }
        }

    }

}

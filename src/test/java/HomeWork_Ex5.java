import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HomeWork_Ex5 {

    @Test
    public void Ex5() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message = response.get("messages[1].message");

        if (message == null) {
            System.out.println("This message is absent");
        } else {
            System.out.println("This is second message: "+message);
        }
    }

}

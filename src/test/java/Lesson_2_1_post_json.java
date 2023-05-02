import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson_2_1_post_json {

    @Test
    public void testRestAssured() {
        Map<String, String> params = new HashMap<>();
        params.put("name","John");

        JsonPath response= RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String name = response.get("answer");

        if (name==null) {
            System.out.println("This key is absent");
        } else {
            System.out.println(name);
        }
    }

}

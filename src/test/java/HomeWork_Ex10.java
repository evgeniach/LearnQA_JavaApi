import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork_Ex10 {

    @ParameterizedTest
    @ValueSource(strings = {"","abcdefghijklmn","abcdefghijklmno","abcdefghijklmnop"})
    public void testHelloMethodNameWithParams(String text) {

        assertTrue(text.length()>15,"Text length less than 15. Text: '"+text+"'");
    }
}

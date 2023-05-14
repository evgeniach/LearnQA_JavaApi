package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    @Step("Assert that: JSON value '{name}' is equal to expected value '{expectedValue}'")
    public static void assertJsonByName(Response Response,String name, int expectedValue) {
        Response.then().assertThat().body("$",hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue,value,"JSON value is not equal to expected value");
    }

    @Step("Assert that: JSON value is equal to expected value")
    public static void assertJsonByName(Response Response,String name, String expectedValue) {
        Response.then().assertThat().body("$",hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue,value,"JSON value is not equal to expected value");
    }

    @Step("Assert that: response text is as expected")
    public static void assertResponseTextEquals(Response Response,String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response test is not as expected");
    }

    @Step("Assert that: response code is as expected")
    public static void assertResponseCodeEquals(Response Response,int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected");
    }

    @Step("Assert that: response has expected field in Json")
    public static void assertJsonHasField(Response Response, String  expectedFieldName) {
        Response.then().assertThat().body("$",hasKey(expectedFieldName));
    }

    @Step("Assert that: response has expected fields in Json")
    public static void assertJsonHasFields(Response Response, String[]  expectedFieldNames) {
        for (String expectedFieldName: expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    @Step("Assert that: response has not expected field in Json")
    public static void assertJsonHasNotField(Response Response, String  unexpectedFieldName) {
        Response.then().assertThat().body("$",not(hasKey(unexpectedFieldName)));
    }

    @Step("Assert that: response has not expected fields in Json")
    public static void assertJsonHasNotFields(Response Response, String[]  expectedFieldNames) {
        for (String expectedFieldName: expectedFieldNames) {
            Assertions.assertJsonHasNotField(Response, expectedFieldName);
        }
    }



}

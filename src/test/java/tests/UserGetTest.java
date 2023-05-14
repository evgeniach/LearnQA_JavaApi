package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User API: Get user cases")
@Feature("Get user data")
public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Story("Negative test for get user data")
    @Description("This test check: a user data can't return without auth.")
    @DisplayName("Test getting user data without auth")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testGetUserDataNotAuth() {

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }

    @Story("Positive test for get user data")
    @Description("This test check: a user data can't return with auth.")
    @DisplayName("Test getting user data with auth")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        Response responseUserData = apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/2",this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        String[] expectedFields = {"username","firstName","lastName","email"};

        Assertions.assertJsonHasFields(responseUserData,expectedFields);
    }

    @Story("Negative test for get user data")
    @Description("This test check: a user data can't return with auth for another user.")
    @DisplayName("Test getting user data with auth for another user")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testGetUserDetailsAuthAsAnotherUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        Response responseUserData = apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/1",this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }

}

package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("User API: user registration")
@Feature("Registration new user")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Story("Negative test for registration user")
    @Description("This test check: a user can't be created if email already exists.")
    @DisplayName("Test registration user with already exists email")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '"+email+"' already exists");
    }

    @Story("Positive test for registration user")
    @Description("This test check: a user can be created")
    @DisplayName("Test successfully registration user")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testCreateUserSuccessfully() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
      }

    @Story("Negative test for registration user")
    @Description("This test check: a user can't be created with incorrect email")
    @DisplayName("Test registration user with incorrect email")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testCreateUserWithIncorrectEmail() {
        String email = "testexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Invalid email format");
    }

    @Story("Negative test for registration user")
    @Description("This test check: a user can't be created without obligatory field")
    @DisplayName("Test registration user without obligatory field")
    @Severity(value=SeverityLevel.BLOCKER)
    @ParameterizedTest
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    public void testCreateUserWithoutObligatoryField(String field) {

        Map<String, String> userData = new HashMap<>();
        userData.put(field,"");
        userData = DataGenerator.getPartOfRegistrationDataWithoutField(userData);

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: "+field);
    }

    @Story("Negative test for registration user")
    @Description("This test check: a user can't be created with short username")
    @DisplayName("Test registration user with short username")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testCreateUserWithShortName() {
        String username = DataGenerator.getRandomStringWithExpectedLength(1);

        Map<String, String> userData = new HashMap<>();
        userData.put("username",username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"The value of 'username' field is too short");
    }

    @Story("Negative test for registration user")
    @Description("This test check: a user can't be created with long username")
    @DisplayName("Test registration user with long username")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testCreateUserWithVeryLongName() {
        String username = DataGenerator.getRandomStringWithExpectedLength(255);

        Map<String, String> userData = new HashMap<>();
        userData.put("username",username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"The value of 'username' field is too long");
    }
}

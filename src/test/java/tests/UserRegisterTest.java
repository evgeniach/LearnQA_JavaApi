package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

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

    @Test
    public void testCreateUserSuccessfully() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
      }

    //email без @
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

    //Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить, что отсутствие любого параметра не дает зарегистрировать пользовател
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

    //Создание пользователя с очень коротким именем в один символ
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

    //Создание пользователя с очень длинным именем - длиннее 250 символов
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

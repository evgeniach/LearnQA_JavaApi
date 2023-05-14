package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User API: Edit user cases")
@Feature("Edit user")
public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Story("Positive test for edit user")
    @Description("This test check: a user can be edited if auth by that user.")
    @DisplayName("Test edit user with correct auth")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testEditJustCreatedTest() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userData);

        String userId = getStringFromJson(responseCreateAuth,"id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //edit
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser= apiCoreRequests
                .makePutRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"),editData);

        //get
        Response responseUserData= apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"firstName",newName);
    }

    @Story("Negative test for edit user")
    @Description("This test check: a user can't be edited without auth.")
    @DisplayName("Test edit user without auth")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testEditUserWithoutAuth() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userData);

        String userId = getStringFromJson(responseCreateAuth,"id");

        //edit
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser= apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,editData);

        Assertions.assertResponseCodeEquals(responseEditUser,400);
        Assertions.assertResponseTextEquals(responseEditUser,"Auth token not supplied");

    }

    @Story("Negative test for edit user")
    @Description("This test check: a user can't be edited if auth by another user.")
    @DisplayName("Test edit user with incorrect auth")
    @Severity(value=SeverityLevel.BLOCKER)
    @Test
    public void testEditUserWithAuthFromAnotherUser() {
        //userOne
        //generate user
        Map<String,String> userOneData = DataGenerator.getRegistrationData();

        Response responseCreateUserOneAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userOneData);

        //login user
        Map<String, String> authUserOneData = new HashMap<>();
        authUserOneData.put("email",userOneData.get("email"));
        authUserOneData.put("password",userOneData.get("password"));

        Response responseGetUserOneAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authUserOneData);

        //userTwo
        //generate user
        Map<String,String> userTwoData = DataGenerator.getRegistrationData();

        Response responseCreateUserTwoAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userTwoData);

        String userTwoId = getStringFromJson(responseCreateUserTwoAuth,"id");

        //login user
        Map<String, String> authUserTwoData = new HashMap<>();
        authUserTwoData.put("email",userTwoData.get("email"));
        authUserTwoData.put("password",userTwoData.get("password"));

        Response responseGetUserTwoAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authUserTwoData);

        //edit userTwo used authorization userOne
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser= apiCoreRequests
                .makePutRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userTwoId,this.getHeader(responseGetUserOneAuth,"x-csrf-token"),this.getCookie(responseGetUserOneAuth,"auth_sid"),editData);

        //get userTwo data
        Response responseUserData= apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userTwoId,this.getHeader(responseGetUserTwoAuth,"x-csrf-token"),this.getCookie(responseGetUserTwoAuth,"auth_sid"));

        Assertions.assertJsonByName(responseUserData,"firstName","learnqa");
    }

    @Story("Negative test for edit user")
    @Description("This test check: a user can't be edited with incorrect email.")
    @DisplayName("Test edit user with incorrect email")
    @Severity(value=SeverityLevel.CRITICAL)
    @Test
    public void testEditUserWithIncorrectEmail() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userData);

        String userId = getStringFromJson(responseCreateAuth,"id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //edit
        String newEmail = "testgmail.com";
        Map<String,String> editData = new HashMap<>();
        editData.put("email",newEmail);

        Response responseEditUser= apiCoreRequests
                .makePutRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"),editData);

        Assertions.assertResponseCodeEquals(responseEditUser,400);
        Assertions.assertResponseTextEquals(responseEditUser,"Invalid email format");
    }

    @Story("Negative test for edit user")
    @Description("This test check: a user can't be edited with incorrect firstName.")
    @DisplayName("Test edit user with incorrect firstName")
    @Severity(value=SeverityLevel.CRITICAL)
    @Test
    public void testEditUserWithIncorrectFirstName() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user",userData);

        String userId = getStringFromJson(responseCreateAuth,"id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        //edit
        String newName = "N";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser= apiCoreRequests
                .makePutRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"),editData);

        Assertions.assertResponseCodeEquals(responseEditUser,400);
        Assertions.assertJsonByName(responseEditUser,"error","Too short value for field firstName");
    }
}


package tests;

import groovyjarjarantlr4.v4.codegen.model.SrcOp;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userData);

        String userId = responseCreateAuth.getString("id");

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

    //Попытаемся изменить данные пользователя, будучи неавторизованными
    @Test
    public void testEditUserWithoutAuth() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userData);

        String userId = responseCreateAuth.getString("id");

        //edit
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser= apiCoreRequests
                .makePutReques("https://playground.learnqa.ru/api/user/"+userId,editData);

        Assertions.assertResponseCodeEquals(responseEditUser,400);
        Assertions.assertResponseTextEquals(responseEditUser,"Auth token not supplied");

    }

    //Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем
    @Test
    public void testEditUserWithAuthFromAnotherUser() {
        //userOne
        //generate user
        Map<String,String> userOneData = DataGenerator.getRegistrationData();

        JsonPath responseCreateUserOneAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userOneData);

        //login user
        Map<String, String> authUserOneData = new HashMap<>();
        authUserOneData.put("email",userOneData.get("email"));
        authUserOneData.put("password",userOneData.get("password"));

        Response responseGetUserOneAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authUserOneData);

        //userTwo
        //generate user
        Map<String,String> userTwoData = DataGenerator.getRegistrationData();

        JsonPath responseCreateUserTwoAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userTwoData);

        String userTwoId = responseCreateUserTwoAuth.getString("id");

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

    //Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @
    @Test
    public void testEditUserWithIncorrectEmail() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userData);

        String userId = responseCreateAuth.getString("id");

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

    //Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ
    @Test
    public void testEditUserWithIncorrectFirstName() {
        //generate user
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth= apiCoreRequests
                .makePostRequestJsonResponse("https://playground.learnqa.ru/api/user",userData);

        String userId = responseCreateAuth.getString("id");

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


package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    //Первый - на попытку удалить пользователя по ID 2. Его данные для авторизации:.
    @Test
    public void testDeleteUserWithId2() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth= apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2",this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser,400);
        Assertions.assertResponseTextEquals(responseDeleteUser,"Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    //Второй - позитивный. Создать пользователя, авторизоваться из-под него, удалить,
    //затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален.
    @Test
    public void testCreateAndDeleteUser() {
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

        //delete
        Response responseDeleteUser= apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        //get
        Response responseUserData= apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userId,this.getHeader(responseGetAuth,"x-csrf-token"),this.getCookie(responseGetAuth,"auth_sid"));

        Assertions.assertResponseCodeEquals(responseUserData,404);
        Assertions.assertResponseTextEquals(responseUserData,"User not found");
    }

    //Третий - негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем.
    @Test
    public void testCreateAndDeleteUserUsedAuthFromAnotherUser() {
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

        //delete userTwo used authorization userOne
        Response responseDeleteUserTwo= apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userTwoId,this.getHeader(responseGetUserOneAuth,"x-csrf-token"),this.getCookie(responseGetUserOneAuth,"auth_sid"));

        //get userTwo data
        Response responseUserData= apiCoreRequests
                .makeGetRequestWithTokenAdnCookie("https://playground.learnqa.ru/api/user/"+userTwoId,this.getHeader(responseGetUserTwoAuth,"x-csrf-token"),this.getCookie(responseGetUserTwoAuth,"auth_sid"));

        Assertions.assertResponseCodeEquals(responseUserData,200);
        Assertions.assertJsonByName(responseUserData,"id",userTwoId);
    }
}

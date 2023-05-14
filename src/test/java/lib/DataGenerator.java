package lib;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    @Step("Generate random email")
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }

    @Step("Generate random string with expected length")
    public static String getRandomStringWithExpectedLength(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    @Step("Generate full registration data for user")
    public static Map<String,String> getRegistrationData() {
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }

    @Step("Generate full registration data for user")
    public static Map<String,String> getRegistrationData(Map<String,String> nonDefaultValues) {
        Map<String,String> defaultValues = DataGenerator.getRegistrationData();

        Map<String,String> userData = new HashMap<>();
        String[] keys =  {"email","password","username","firstName","lastName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key,nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    @Step("Generate part of registration data for user")
    public static Map<String,String> getPartOfRegistrationDataWithoutField(Map<String,String> nonDefaultValues) {
        Map<String,String> defaultValues = DataGenerator.getRegistrationData();

        Map<String,String> userData = new HashMap<>();
        String[] keys =  {"email","password","username","firstName","lastName"};
        for (String key : keys) {
            if (!nonDefaultValues.containsKey(key)) {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}

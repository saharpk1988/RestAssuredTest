package com.example.myfirstws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

public class UsersWebServiceEndpointTest {

    private final String CONTEXT_PATH="/my-first-ws";
    private final String EMAIL_ADDRESS="sahar.pourkarimi@yahoo.com";
    private final String JSON="application/json";
    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

    }

    @Test
    final void testUserLogin(){
        Map<String,String> loginDetails=new HashMap<>();
        loginDetails.put("email",EMAIL_ADDRESS);
        loginDetails.put("password","123");

        Response response=given().
                accept(JSON)
                .contentType(JSON)
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH+"/users/login")
                .then()
                .statusCode(200)
                .extract().response();
        String authorizationHeader=response.header("Authorization");
        String userId=response.header("UserID");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }
}

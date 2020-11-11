package com.example.myfirstws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersWebServiceEndpointTest {

    private final String CONTEXT_PATH="/my-first-ws";
    private final String EMAIL_ADDRESS="sahar.pourkarimi@yahoo.com";
    private final String JSON="application/json";
    private static String userId;
    private static String authorizationHeader;
    private static List<Map<String,String>> addresses;
    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

    }

    /**
     * testUserLogin()
     * Testing the User Login API Call
     */
    @Test
    final void a(){
        Map<String,String> loginDetails=new HashMap<>();
        loginDetails.put("email",EMAIL_ADDRESS);
        loginDetails.put("password","123");

        Response response=given()
                .accept(JSON)
                .contentType(JSON)
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH+"/users/login")
                .then()
                .statusCode(200)
                .extract().response();
        authorizationHeader=response.header("Authorization");
        userId=response.header("UserID");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);
    }

    /**
     * testGetUserDetails()
     * Testing the Get User Details API call
     */
    @Test
    final void b(){
        Response response=given()
                .pathParam("id",userId)
                .header("Authorization",authorizationHeader)
                .accept(JSON)
                .when()
                .get(CONTEXT_PATH+"/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        String userPublicId=response.jsonPath().getString("userId");
        String userEmail=response.jsonPath().getString("email");
        String firstName=response.jsonPath().getString("firstName");
        String lastName=response.jsonPath().getString("lastName");
        addresses=response.jsonPath().getList("addresses");
        String addressId=addresses.get(0).get("addressId");


        assertNotNull(userPublicId);
        assertNotNull(userEmail);
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertEquals(EMAIL_ADDRESS,userEmail);
        assertTrue(addresses.size()==2);
        assertNotNull(addressId);
        assertTrue(addressId.length()==30);
    }

    /**
     * testUpdateUserDetails()
     * Testing the Update User Details API call
     */
    @Test
    final void c(){

        Map<String, String> userDetails=new HashMap<>();
        userDetails.put("firstName","Sahar");
        userDetails.put("lastName","PkSol");
        userDetails.put("password","321");

        Response response=given()
                .pathParam("id",userId)
                .header("Authorization",authorizationHeader)
                .accept(JSON)
                .contentType(JSON)
                .body(userDetails)
                .when()
                .put(CONTEXT_PATH+"/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        String updatedFirstName=response.jsonPath().getString("firstName");
        String updatedLastName=response.jsonPath().getString("lastName");
        List<Map<String, String>> storedAddresses=response.jsonPath().getList("addresses");

        assertEquals("Sahar",updatedFirstName);
        assertTrue(updatedLastName.equalsIgnoreCase("pksol"));
        assertNotNull(storedAddresses);
        assertTrue(addresses.size()==storedAddresses.size());
        assertEquals(addresses.get(0).get("streetName"),storedAddresses.get(0).get("streetName"));

    }
}

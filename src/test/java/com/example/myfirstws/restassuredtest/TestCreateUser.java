package com.example.myfirstws.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateUser {

    private final String CONTEXT_PATH = "/my-first-ws";

    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    final void testCreateUser() {
        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Vancouver");
        shippingAddress.put("country", "canada");
        shippingAddress.put("streetName", "123 Street name");
        shippingAddress.put("postalCode", "123456");
        shippingAddress.put("type", "shipping");

        Map<String,Object> billingAddress=new HashMap<>();
        billingAddress.put("city", "Vancouver");
        billingAddress.put("country", "canada");
        billingAddress.put("streetName", "123 Street name");
        billingAddress.put("postalCode", "123456");
        billingAddress.put("type", "billing");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Sarar");
        userDetails.put("lastName", "Pk");
        userDetails.put("email", "sahar.pourkarimi@yahoo.com");
        userDetails.put("password", "123");
        userDetails.put("addresses", userAddresses);

        Response response = given().
                contentType("application/json").
                accept("application/json").
                body(userDetails).
                when().
                post(CONTEXT_PATH + "/users").
                then().
                statusCode(200).
                contentType("application/json").
                extract().
                response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);

        String bodyString=response.body().asString();

        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses=responseBodyJson.getJSONArray("addresses");

            assertNotNull(addresses);
            assertTrue(addresses.length()==2);

            String addressId=addresses.getJSONObject(0).getString("addressId");
            assertNotNull(addressId);
            assertTrue(addressId.length()==30);

        }catch (JSONException exception){
            fail(exception.getMessage());
        }
    }
}

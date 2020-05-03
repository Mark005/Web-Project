package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import com.nncompany.api.model.enums.Gender;
import com.nncompany.api.model.wrappers.ResponseList;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServletTest extends AbstractServletTest{
    private ObjectMapper objectMapper;
    private Response response;
    private UserCreds testUserCreds;
    private User testUser;
    private Integer testUserId;
    private String testUserToken;
    private Map<String, Integer> params;

    @Before
    public void loadValues(){
        objectMapper = new ObjectMapper();
        params = new HashMap<>();
            params.put("page", 0);
            params.put("pageSize", 100);
        testUser = new User();
            testUser.setName("New");
            testUser.setSurname("User");
            testUser.setGender(Gender.MALE);
            testUser.setCertificateNumber(666);
            testUser.setDateEmployment(new Date());
            testUser.setProfession("slave");
            testUser.setAdmin(true);
        testUserCreds = new UserCreds();
            testUserCreds.setLogin("newUniqueLogin");
            testUserCreds.setPass("newUniqueLogin");
            testUserCreds.setUser(testUser);
    }

    @Test
    public void A_addUser(){
        given()
                .body(testUserCreds)
                .post("/api/rest/registration/user")
                .then().statusCode(201);
    }

    @Test
    public void B_getLoggedUser(){
        testUserToken = getToken(testUserCreds);
        given()
                .header("token", testUserToken)
                .get("/api/rest/creds/user")
                .then().statusCode(200).assertThat()
                .body("name", equalTo(testUser.getName()))
                .body("surname", equalTo(testUser.getSurname()))
                .body("certificateNumber", equalTo(testUser.getCertificateNumber()));
    }

    @Test
    public void C_getAllUsers() throws JsonProcessingException {
        testUserToken = getToken(testUserCreds);
        response = given()
                .header("token", testUserToken)
                .queryParams(params)
                .get("/api/rest/creds/users");
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        ResponseList<User> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<User>>() {});
        assertTrue(responseList.getTotal().longValue() == responseList.getList().size());
    }

    @Test
    public void D_getUser() throws JsonProcessingException {
        testUserToken = getToken(testUserCreds);
        testUserId = given()
                .header("token", testUserToken)
                .get("/api/rest/creds/user")
                .jsonPath().getInt("id");
        given()
                .header("token", testUserToken)
                .get("/api/rest/creds/users/"+ testUserId)
                .then().assertThat()
                .body("name", equalTo(testUser.getName()))
                .body("surname", equalTo(testUser.getSurname()))
                .body("certificateNumber", equalTo(testUser.getCertificateNumber()));
    }

    @Test
    public void E_updateUser(){
        testUserToken = getToken(testUserCreds);
        testUserId = given()
                .header("token", testUserToken)
                .get("/api/rest/creds/user")
                .jsonPath().getInt("id");

        testUser.setName("a5_e5jkl135ekg");
        given()
                .header("token", USER_TOKEN)
                .body(testUser)
                .put("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(403);

        given()
                .header("token", ADMIN_TOKEN)
                .body(testUser)
                .put("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(200);

        given()
                .header("token", testUserToken)
                .body(testUser)
                .put("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(200);

        given()
                .header("token", testUserToken)
                .get("/api/rest/creds/users/"+ testUserId)
                .then().assertThat()
                .body("name", equalTo(testUser.getName()));
    }

    @Test
    public void F_deleteUser(){
        testUserToken = getToken(testUserCreds);
        testUserId = given()
                .header("token", testUserToken)
                .get("/api/rest/creds/user")
                .jsonPath().getInt("id");

        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(403);
        given()
                .header("token", testUserToken)
                .delete("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(204);
    }
}

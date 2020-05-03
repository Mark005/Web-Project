package com.nncompany.servlets.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import com.nncompany.api.model.enums.Gender;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationServletTest extends AbstractServletTest {

    private UserCreds testUserCreds;
    private User testUser;
    private Integer testUserId;
    private String testUserToken;
    private Map<String, Integer> params;

    @Before
    public void loadValues(){
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
    public void A_checkLoginBeforeAddingUser(){
        given()
                .body(testUserCreds)
                .post("/api/rest/registration/checkLogin")
                .then().statusCode(200)
                .body("response", equalTo(false));
    }

    @Test
    public void B_addUser(){
        given()
                .body(testUserCreds)
                .post("/api/rest/registration/user")
                .then().statusCode(201);
    }

    @Test
    public void C_checkLoginAfterAddingUser(){
        given()
                .body(testUserCreds)
                .post("/api/rest/registration/checkLogin")
                .then().statusCode(200)
                .body("response", equalTo(true));
    }

    @Test
    public void D_deleteUser(){
        testUserToken = getToken(testUserCreds);
        testUserId = given()
                .header("token", testUserToken)
                .get("/api/rest/creds/user")
                .jsonPath().getInt("id");
        given()
                .header("token", testUserToken)
                .delete("/api/rest/creds/users/"+ testUserId)
                .then().statusCode(204);
    }
}

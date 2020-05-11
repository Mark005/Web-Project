package com.nncompany.servlets.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import com.nncompany.api.model.enums.Gender;
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
import static org.hamcrest.core.IsNull.notNullValue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationServletTest extends AbstractServletTest {

    private UserCreds testUserCreds;
    private static Integer testUserId;
    private User testUser;
    private String testUserToken;

    @Before
    public void loadValues(){
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
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("response", equalTo(false));
    }

    @Test
    public void B_addUser(){
        testUserId = given()
                            .body(testUserCreds)
                            .post("/api/rest/registration/user")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_CREATED)
                            .body("id", notNullValue())
                            .body("name", equalTo(testUser.getName()))
                            .body("surname", equalTo(testUser.getSurname()))
                            .body("gender", equalTo(testUser.getGender().name()))
                            .body("certificateNumber", equalTo(testUser.getCertificateNumber()))
                            .body("dateEmployment", equalTo(testUser.getDateEmployment().getTime()))
                            .body("profession", equalTo(testUser.getProfession()))
                            .body("admin", equalTo(false))
                            .extract()
                            .path("id");
    }

    @Test
    public void C_checkLoginAfterAddingUser(){
        given()
                .body(testUserCreds)
                .post("/api/rest/registration/checkLogin")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("response", equalTo(true));
    }

    @Test
    public void D_deleteUser(){
        testUserToken = getToken(testUserCreds);

        given()
                .header("token", ANOTHER_USER_TOKEN)
                .delete("/api/rest/creds/users/"+ testUserId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", testUserToken)
                .delete("/api/rest/creds/users/"+ testUserId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/user/"+ testUserId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

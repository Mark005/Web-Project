package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import com.nncompany.api.model.enums.Gender;
import com.nncompany.api.model.wrappers.ResponseList;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServletTest extends AbstractServletTest{
    private UserCreds userCreds;
    private User user;
    private static Integer userId;
    private static String userToken;

    @Before
    public void loadValues(){
        Calendar c = new GregorianCalendar(2000, 5, 5);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));

        user = new User();
            user.setName("New");
            user.setSurname("User");
            user.setGender(Gender.MALE);
            user.setCertificateNumber(666);
            user.setDateEmployment(c.getTime());
            user.setProfession("slave");
            user.setAdmin(true);
        userCreds = new UserCreds();
            userCreds.setLogin("newUniqueLogin");
            userCreds.setPass("newUniqueLogin");
            userCreds.setUser(user);
    }

    @Test
    public void A_addUser() {
        userId  = given()
                        .body(userCreds)
                        .post("/api/rest/registration/user")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("id", notNullValue())
                        .body("name", equalTo(user.getName()))
                        .body("gender", equalTo(user.getGender().name()))
                        .body("surname", equalTo(user.getSurname()))
                        .body("profession", equalTo(user.getProfession()))
                        .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                        .body("certificateNumber", equalTo(user.getCertificateNumber()))
                        .body("admin", equalTo(false))
                        .extract()
                        .path("id");
    }

    @Test
    public void B_getLoggedUser() {
        userToken = getToken(userCreds);
        given()
                .header("token", userToken)
                .get("/api/rest/creds/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("name", equalTo(user.getName()))
                .body("gender", equalTo(user.getGender().name()))
                .body("surname", equalTo(user.getSurname()))
                .body("profession", equalTo(user.getProfession()))
                .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                .body("certificateNumber", equalTo(user.getCertificateNumber()))
                .body("admin", equalTo(false));
    }

    @Test
    public void C_getAllUsers() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = given()
                            .header("token", userToken)
                            .queryParams(PAGINATION_PARAMS)
                            .get("/api/rest/creds/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .extract()
                            .asString();
        ResponseList<User> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<User>>() {});
        assertTrue(responseList.getTotal().longValue() == responseList.getList().size());
    }

    @Test
    public void D_getUser() {
        given()
                .header("token", userToken)
                .get("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(userId))
                .body("name", equalTo(user.getName()))
                .body("gender", equalTo(user.getGender().name()))
                .body("surname", equalTo(user.getSurname()))
                .body("profession", equalTo(user.getProfession()))
                .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                .body("certificateNumber", equalTo(user.getCertificateNumber()))
                .body("admin", equalTo(false));
    }

    @Test
    public void D_findUsers() {
        given()
                .header("token", userToken)
                .queryParams(PAGINATION_PARAMS)
                .queryParam("searchString", user.getName() + " " + user.getSurname())
                .get("/api/rest/creds/users/search")
                .then()
                .assertThat()
                .body("total", equalTo(1))
                .body("list.id", notNullValue())
                .body("list.name", hasItem(user.getName()))
                .body("list.gender", hasItem(user.getGender().name()))
                .body("list.surname", hasItem(user.getSurname()))
                .body("list.profession", hasItem(user.getProfession()))
                .body("list.dateEmployment", hasItem(user.getDateEmployment().getTime()))
                .body("list.certificateNumber", hasItem(user.getCertificateNumber()))
                .body("list.admin", hasItem(false));

        given()
                .header("token", userToken)
                .queryParams(PAGINATION_PARAMS)
                .queryParam("searchString", user.getCertificateNumber())
                .get("/api/rest/creds/users/search")
                .then()
                .assertThat()
                .body("total", equalTo(1))
                .body("list.id", notNullValue())
                .body("list.name", hasItem(user.getName()))
                .body("list.gender", hasItem(user.getGender().name()))
                .body("list.surname", hasItem(user.getSurname()))
                .body("list.profession", hasItem(user.getProfession()))
                .body("list.dateEmployment", hasItem(user.getDateEmployment().getTime()))
                .body("list.certificateNumber", hasItem(user.getCertificateNumber()))
                .body("list.admin", hasItem(false));
    }

    @Test
    public void E_updateUser(){
        user.setName("a5_e5jkl135ekg");
        user.setAdmin(true);
        given()
                .header("token", USER_TOKEN)
                .body(user)
                .put("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", userToken)
                .body(user)
                .put("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(userId))
                .body("name", equalTo(user.getName()))
                .body("gender", equalTo(user.getGender().name()))
                .body("surname", equalTo(user.getSurname()))
                .body("profession", equalTo(user.getProfession()))
                .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                .body("certificateNumber", equalTo(user.getCertificateNumber()))
                .body("admin", equalTo(false));

        user.setName("O_______O - wow");
        user.setAdmin(true);

        given()
                .header("token", ADMIN_TOKEN)
                .body(user)
                .put("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(userId))
                .body("name", equalTo(user.getName()))
                .body("gender", equalTo(user.getGender().name()))
                .body("surname", equalTo(user.getSurname()))
                .body("profession", equalTo(user.getProfession()))
                .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                .body("certificateNumber", equalTo(user.getCertificateNumber()))
                .body("admin", equalTo(true));

        given()
                .header("token", userToken)
                .get("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(userId))
                .body("name", equalTo(user.getName()))
                .body("gender", equalTo(user.getGender().name()))
                .body("surname", equalTo(user.getSurname()))
                .body("profession", equalTo(user.getProfession()))
                .body("dateEmployment", equalTo(user.getDateEmployment().getTime()))
                .body("certificateNumber", equalTo(user.getCertificateNumber()))
                .body("admin", equalTo(true));
    }

    @Test
    public void F_deleteUser(){
        given()
                .header("token", ANOTHER_USER_TOKEN)
                .delete("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", userToken)
                .delete("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", ANOTHER_USER_TOKEN)
                .get("/api/rest/creds/users/"+ userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

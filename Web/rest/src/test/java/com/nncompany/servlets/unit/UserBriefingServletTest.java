package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;
import com.nncompany.api.model.wrappers.ResponseList;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserBriefingServletTest extends AbstractServletTest{

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserBriefing userBriefing;
    private static Integer userBriefingId;

    @Before
    public void loadValues() throws JsonProcessingException {
        User testUser = getUserByToken(USER_TOKEN);

        String json = given()
                            .header("token", USER_TOKEN)
                            .get("/api/rest/creds/briefings/" + 2)
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .extract()
                            .asString();
        Briefing testBriefing = objectMapper.readValue(json, Briefing.class);
        userBriefing = new UserBriefing();
            userBriefing.setUser(testUser);
            userBriefing.setBriefing(testBriefing);
            userBriefing.setLastDate(new Date(2009,9,9));
    }

    @Test
    public void getBriefingsByUser() throws JsonProcessingException {
        String json = given()
                            .header("token", USER_TOKEN)
                            .get("/api/rest/creds/user-briefing/conducted-by-user/" + userBriefing.getUser().getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .extract()
                            .asString();
        List<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<List<UserBriefing>>() {});
        for(UserBriefing currentUB : responseList){
            assertEquals(currentUB.getUser(), userBriefing.getUser());
        }
    }

    @Test
    public void getUsersByBriefing() throws JsonProcessingException {
        String json = given()
                            .header("token", USER_TOKEN)
                            .get("/api/rest/creds/user-briefing/conducted-by-briefing/" + userBriefing.getBriefing().getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .extract()
                            .asString();
        List<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<List<UserBriefing>>() {});
        for(UserBriefing currentUB : responseList){
            assertEquals(currentUB.getBriefing(), userBriefing.getBriefing());
        }
    }

    @Test
    public void A_addConducting() {
        given()
                .header("token", USER_TOKEN)
                .body(userBriefing)
                .post("/api/rest/creds/user-briefing/conducted")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        userBriefingId = given()
                                .header("token", ADMIN_TOKEN)
                                .body(userBriefing)
                                .post("/api/rest/creds/user-briefing/conducted")
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_CREATED)
                                .body("id", notNullValue())
                                .body("user.id", equalTo(userBriefing.getUser().getId()))
                                .body("briefing.id", equalTo(userBriefing.getBriefing().getId()))
                                .body("lastDate", equalTo(userBriefing.getLastDate().getTime()))
                                .extract()
                                .path("id");
    }

    @Test
    public void B_getAllConductedBriefings() throws JsonProcessingException {
        String json = given()
                            .header("token", USER_TOKEN)
                            .queryParams(PAGINATION_PARAMS)
                            .queryParam("sort", UserBriefingSort.id)
                            .queryParam("direction", Direction.asc)
                            .get("/api/rest/creds/user-briefing/conducted")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .extract()
                            .asString();
        ResponseList<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<UserBriefing>>() {});
        userBriefingId = responseList.getList().get(responseList.getTotal()-1).getId();
    }

    @Test
    public void C_updateBriefingsConductionDay() {
        userBriefing.setLastDate(new Date(2001,1,1));
        given()
                .header("token", USER_TOKEN)
                .body(userBriefing)
                .patch("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", ADMIN_TOKEN)
                .body(userBriefing)
                .patch("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("user.id", equalTo(userBriefing.getUser().getId()))
                .body("briefing.id", equalTo(userBriefing.getBriefing().getId()))
                .body("lastDate", equalTo(userBriefing.getLastDate().getTime()));
    }

    @Test
    public void D_getConductedById() throws JsonProcessingException {
        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("user.id", equalTo(userBriefing.getUser().getId()))
                .body("briefing.id", equalTo(userBriefing.getBriefing().getId()))
                .body("lastDate", not(userBriefing.getLastDate().getTime()));

    }

    @Test
    public void E_deleteConduction() {
        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", ADMIN_TOKEN)
                .delete("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/user-briefing/conducted/" + userBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

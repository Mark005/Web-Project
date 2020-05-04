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
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserBriefingServletTest extends AbstractServletTest{

    private ObjectMapper objectMapper = new ObjectMapper();
    private Briefing testBriefing;
    private User testUser;
    private Date testDate = new Date(2009,9,9);
    private Date anotherTestDate = new Date(2001,1,1);
    private UserBriefing testUserBriefing;
    private static Integer lastAddedId;

    @Before
    public void loadValues() throws JsonProcessingException {
        testUser = getUserByToken(USER_TOKEN);

        String briefingJson = given() .header("token", USER_TOKEN)
                                      .get("/api/rest/creds/briefings/" + 2)
                                      .asString();
        testBriefing = objectMapper.readValue(briefingJson, Briefing.class);

        testUserBriefing = new UserBriefing();
            testUserBriefing.setUser(testUser);
            testUserBriefing.setBriefing(testBriefing);
            testUserBriefing.setLastDate(testDate);
    }

    @Test
    public void getBriefingsByUser() throws JsonProcessingException {
        Response response = given() .header("token", USER_TOKEN)
                                    .get("/api/rest/creds/user-briefing/conducted-by-user/" + testUser.getId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        List<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<List<UserBriefing>>() {});
        for(UserBriefing currentUB : responseList){
            assertEquals(currentUB.getUser(), testUser);
        }
    }

    @Test
    public void getUsersByBriefing() throws JsonProcessingException {
        Response response = given() .header("token", USER_TOKEN)
                                    .get("/api/rest/creds/user-briefing/conducted-by-briefing/" + testBriefing.getId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();

        List<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<List<UserBriefing>>() {});
        for(UserBriefing currentUB : responseList){
            assertEquals(currentUB.getBriefing(), testBriefing);
        }
    }

    @Test
    public void A_addConducting() {
        given() .header("token", USER_TOKEN)
                .body(testUserBriefing)
                .post("/api/rest/creds/user-briefing/conducted")
                .then().statusCode(HttpStatus.SC_FORBIDDEN);

        given() .header("token", ADMIN_TOKEN)
                .body(testUserBriefing)
                .post("/api/rest/creds/user-briefing/conducted")
                .then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void B_getAllConductedBriefings() throws JsonProcessingException {
        Response response = given() .header("token", USER_TOKEN)
                                    .queryParams(PAGINATION_PARAMS)
                                    .queryParam("sort", UserBriefingSort.id)
                                    .queryParam("direction", Direction.asc)
                                    .get("/api/rest/creds/user-briefing/conducted");
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        String json = response.asString();
        ResponseList<UserBriefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<UserBriefing>>() {});
        assertNotNull(responseList);
        lastAddedId = responseList.getList().get(responseList.getTotal()-1).getId();
    }

    @Test
    public void C_updateBriefingsConductionDay() {
        testUserBriefing.setLastDate(anotherTestDate);
        given() .header("token", USER_TOKEN)
                .body(testUserBriefing)
                .patch("/api/rest/creds/user-briefing/conducted/" + lastAddedId)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);

        given() .header("token", ADMIN_TOKEN)
                .body(testUserBriefing)
                .patch("/api/rest/creds/user-briefing/conducted/" + lastAddedId)
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void D_getConductedById() throws JsonProcessingException {
        Response response = given() .header("token", USER_TOKEN)
                                    .get("/api/rest/creds/user-briefing/conducted/" + lastAddedId);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        String json = response.asString();
        UserBriefing userBriefing = objectMapper.readValue(json, UserBriefing.class);
        assertEquals(userBriefing.getLastDate(), anotherTestDate);
    }

    @Test
    public void E_deleteConduction() {
        given() .header("token", USER_TOKEN)
                .delete("/api/rest/creds/user-briefing/conducted/" + lastAddedId)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);

        given() .header("token", ADMIN_TOKEN)
                .delete("/api/rest/creds/user-briefing/conducted/" + lastAddedId)
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        given() .header("token", USER_TOKEN)
                .get("/api/rest/creds/user-briefing/conducted/" + lastAddedId)
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

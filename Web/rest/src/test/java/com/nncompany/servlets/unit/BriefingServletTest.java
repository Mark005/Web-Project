package com.nncompany.servlets.unit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.wrappers.ResponseList;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsNot.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BriefingServletTest extends AbstractServletTest{
    private static Integer testBriefingId;
    private Briefing testBriefing;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void loadValues(){
        testBriefing = new Briefing("test briefing", 2);
    }

    @Test
    public void A_getBriefings() throws IOException {
        Response response = given() .header("token", USER_TOKEN)
                                    .queryParams(PAGINATION_PARAMS)
                                    .get("/api/rest/creds/briefings");
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        ResponseList<Briefing> responseList = objectMapper.readValue(response.getBody().asString(), ResponseList.class);
        assertNotNull(responseList);
        assertTrue(responseList.getList().size() == responseList.getTotal());
    }

    @Test
    public void B_addBriefing() throws IOException {
         given()
                 .header("token", USER_TOKEN)
                 .body(testBriefing)
                 .post("/api/rest/creds/briefings")
                 .then()
                 .statusCode(HttpStatus.SC_FORBIDDEN);

        testBriefingId = given()
                                .header("token", ADMIN_TOKEN)
                                .body(testBriefing)
                                .post("/api/rest/creds/briefings")
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_CREATED)
                                .body("id", notNullValue())
                                .body("name", equalTo(testBriefing.getName()))
                                .body("intervalInMonths", equalTo(testBriefing.getIntervalInMonths()))
                                .extract()
                                .path("id");
    }

    @Test
    public void C_changeBriefing() throws IOException {
        testBriefing.setName("NEW name");
        given()
                .header("token", USER_TOKEN)
                .body(testBriefing)
                .put("/api/rest/creds/briefings/" + testBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", ADMIN_TOKEN)
                .body(testBriefing)
                .put("/api/rest/creds/briefings/" + testBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("name", equalTo(testBriefing.getName()))
                .body("intervalInMonths", equalTo(testBriefing.getIntervalInMonths()));
    }

    @Test
    public void D_deleteBriefing() throws JsonProcessingException {
        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/briefings/" + testBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", ADMIN_TOKEN)
                .delete("/api/rest/creds/briefings/" + testBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/briefings/" + testBriefingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);

    }
}

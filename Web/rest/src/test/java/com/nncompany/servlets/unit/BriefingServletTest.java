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
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BriefingServletTest extends AbstractServletTest{
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
        ResponseList<Briefing> responseList = objectMapper.readValue(response.getBody().asString(), ResponseList.class);

        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        assertNotNull(responseList);
        assertTrue(responseList.getList().size() == responseList.getTotal());
    }

    @Test
    public void B_addBriefing() throws IOException {
        Integer totalCount = getTotalCount();
        Response response = given() .header("token", USER_TOKEN)
                                    .body(testBriefing)
                                    .post("/api/rest/creds/briefings");
        assertEquals(response.getStatusCode(), HttpStatus.SC_FORBIDDEN);

        response = given() .header("token", ADMIN_TOKEN)
                            .body(testBriefing)
                            .post("/api/rest/creds/briefings");
        assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED);
        assertTrue(++totalCount == getTotalCount().intValue());

        response = given() .header("token", USER_TOKEN)
                            .queryParams(PAGINATION_PARAMS)
                            .get("/api/rest/creds/briefings");
        String json =  response.getBody().asString();
        ResponseList<Briefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<Briefing>>() {});
        Briefing lastAdded = responseList.getList().get(responseList.getTotal()-1);
        assertEquals(lastAdded.getName(), testBriefing.getName());
        assertEquals(lastAdded.getIntervalInMonths(), testBriefing.getIntervalInMonths());
    }

    @Test
    public void C_changeBriefing() throws IOException {
        testBriefing.setName("NEW name");
        Response response = given() .header("token", USER_TOKEN)
                                    .body(testBriefing)
                                    .put("/api/rest/creds/briefings/" + getLastId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_FORBIDDEN);

        response = given() .header("token", ADMIN_TOKEN)
                            .body(testBriefing)
                            .put("/api/rest/creds/briefings/" + getLastId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);

        response = given() .header("token", USER_TOKEN)
                .queryParams(PAGINATION_PARAMS)
                .get("/api/rest/creds/briefings");
        String json = response.getBody().asString();
        ResponseList<Briefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<Briefing>>() {});
        Briefing lastAdded = responseList.getList().get(responseList.getTotal()-1);
        assertEquals(lastAdded.getName(), testBriefing.getName());
        assertEquals(lastAdded.getIntervalInMonths(), testBriefing.getIntervalInMonths());
    }

    @Test
    public void D_deleteBriefing() throws JsonProcessingException {
        Integer totalCount = getTotalCount();
        Response response = given() .header("token", USER_TOKEN)
                                    .body(testBriefing)
                                    .delete("/api/rest/creds/briefings/" + getLastId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_FORBIDDEN);

        response = given() .header("token", ADMIN_TOKEN)
                            .body(testBriefing)
                            .delete("/api/rest/creds/briefings/" + getLastId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT);
        assertTrue(--totalCount == getTotalCount().intValue());

        response = given() .header("token", USER_TOKEN)
                .queryParams(PAGINATION_PARAMS)
                .get("/api/rest/creds/briefings");
        String json = response.getBody().asString();
        ResponseList<Briefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<Briefing>>() {});

        Briefing lastAdded = responseList.getList().get(responseList.getTotal()-1);
        assertNotEquals(lastAdded.getName(), testBriefing.getName());
        assertNotEquals(lastAdded.getIntervalInMonths(), testBriefing.getIntervalInMonths());
    }
    private Integer getTotalCount(){
        String total = given() .header("token", USER_TOKEN)
                                .queryParams(PAGINATION_PARAMS)
                                .get("/api/rest/creds/briefings")
                                .jsonPath().getString("total");
        return Integer.parseInt(total);
    }

    private Integer getLastId() throws JsonProcessingException {
        String json = given() .header("token", USER_TOKEN)
                                .queryParams(PAGINATION_PARAMS)
                                .get("/api/rest/creds/briefings")
                                .asString();
        ResponseList<Briefing> responseList = objectMapper.readValue(json, new TypeReference<ResponseList<Briefing>>() {});
        return responseList.getList().get(responseList.getTotal()-1).getId();
    }
}

package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserCreds;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-application-context-test.xml",
                                    "classpath:application-context-test.xml"})
@WebAppConfiguration()
public abstract class AbstractServletTest {
    protected String ADMIN_TOKEN;
    protected String USER_TOKEN;
    protected String ANOTHER_USER_TOKEN;
    protected final String ROOT_URL = "http://localhost:8080/rest_war_exploded";
    protected Map<String, Integer> PAGINATION_PARAMS = new HashMap<>();

    @Before
    public void init(){
        RestAssured.baseURI = ROOT_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
                                                                   .setAccept(ContentType.JSON)
                                                                   .build();
        ADMIN_TOKEN =  getToken(new UserCreds("admin", "admin"));
        USER_TOKEN = getToken(new UserCreds("user", "user"));
        ANOTHER_USER_TOKEN = getToken(new UserCreds("aaa", "456"));
        PAGINATION_PARAMS.put("page", 0);
        PAGINATION_PARAMS.put("pageSize", 100);
    }

    @Test
    public void autorizationTest(){
        assertNotNull(ADMIN_TOKEN);
        assertNotNull(USER_TOKEN);
        assertNotNull(ANOTHER_USER_TOKEN);
    }

    protected String getToken(UserCreds userCreds){
        return  given()
                        .body(userCreds)
                        .post("/api/rest/logIn")
                        .jsonPath()
                        .getString("value");
    }

    protected User getUserByToken(String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = given()
                             .header("token", token)
                             .get("/api/rest/creds/user")
                             .asString();
        return objectMapper.readValue(json, User.class);
    }
}

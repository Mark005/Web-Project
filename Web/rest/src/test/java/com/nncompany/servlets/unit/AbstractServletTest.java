package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nncompany.api.model.entities.UserCreds;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-application-context-test.xml",
                                    "classpath:application-context-test.xml"})
@WebAppConfiguration()
public abstract class AbstractServletTest {
    protected String ADMIN_TOKEN;
    protected String USER_TOKEN;
    protected final String ROOT_URL = "http://localhost:8080/rest_war_exploded";

    @Before
    public void init() throws JsonProcessingException {
        RestAssured.baseURI = ROOT_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        UserCreds adminCreds = new UserCreds("admin", "admin");
        UserCreds userCreds = new UserCreds("user", "user");


        ADMIN_TOKEN = given()
                             .body(adminCreds)
                             .post("/api/rest/logIn")
                             .jsonPath()
                                .getString("value");

        USER_TOKEN = given()
                            .body(userCreds)
                            .post("/api/rest/logIn")
                            .jsonPath()
                                .getString("value");
    }



    protected Response get(String url, String token, Map params) throws JsonProcessingException {
        return given()
                .header("token", token)
                .queryParams(params)
                .get(url);
    }

    protected Response post(String url, Object o) throws JsonProcessingException {
        return given()
                .contentType(ContentType.JSON)
                .body(o)
                .post(url);
    }

    protected Response post(String url, String token, Map params) throws JsonProcessingException {
        return given()
                .header("token", token)
                .queryParams(params)
                .get(url);
    }
}

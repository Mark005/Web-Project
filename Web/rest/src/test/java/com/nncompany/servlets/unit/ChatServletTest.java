package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.wrappers.ResponseList;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatServletTest extends AbstractServletTest{
    private User user;
    private Message testMessage;
    private Map<String,Integer> params;
    private final String MESSAGE_TEXT = "tytyhrJKHNjnjnjNUNJn";
    private final String NEW_MESSAGE_TEXT = "pe_lkm5b-MK0bthm";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void loadValues() throws JsonProcessingException {
        params = new HashMap<>();
            params.put("page", 0);
            params.put("pageSize", 100);
        user = getUserByToken(USER_TOKEN);
        testMessage = new Message(MESSAGE_TEXT, user, null);
    }

    @Test
    public void A_addMessage(){
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .post("/api/rest/creds/chat")
                .then().statusCode(201);
    }

    @Test
    public void B_getChat() throws JsonProcessingException {
        ResponseList<Message> allChat = getMessages();
        assertEquals(allChat.getList().size(), allChat.getTotal().intValue());
    }

    @Test
    public void C_getMessage() throws JsonProcessingException {
        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/chat/" + getLastMessage().getId())
                .then().assertThat()
                .body("text", equalTo(MESSAGE_TEXT));
    }

    @Test
    public void D_changeMessage() throws JsonProcessingException {
        Integer id = getLastMessage().getId();
        testMessage.setText(NEW_MESSAGE_TEXT);
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/chat/" + id)
                .then().assertThat()
                .statusCode(200);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/chat/" + id)
                .then().assertThat()
                .statusCode(200)
                .body("text", equalTo(NEW_MESSAGE_TEXT));
    }

    @Test
    public void E_deleteMessage() throws JsonProcessingException {
        Integer id = getLastMessage().getId();
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .delete("/api/rest/creds/chat/" + id)
                .then().assertThat()
                .statusCode(204);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/chat/" + id)
                .then().assertThat()
                .statusCode(404);
    }

    private ResponseList<Message> getMessages() throws JsonProcessingException {
        Response response = given()
                .header("token", USER_TOKEN)
                .queryParams(params)
                .get("/api/rest/creds/chat");
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        return  objectMapper.readValue(json, new TypeReference<ResponseList<Message>>() {});
    }

    private Message getLastMessage() throws JsonProcessingException {
        ResponseList<Message> allChat = getMessages();
        assertEquals(allChat.getList().size(), allChat.getTotal().intValue());
        return allChat.getList().get(allChat.getList().size()-1);
    }
}

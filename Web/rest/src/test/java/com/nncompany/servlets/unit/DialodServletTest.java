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
public class DialodServletTest extends AbstractServletTest {
    private User userOne;
    private User userTwo;
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

        userOne = getUserByToken(USER_TOKEN);
        userTwo = getUserByToken(ANOTHER_USER_TOKEN);

        testMessage = new Message(MESSAGE_TEXT, userOne, userTwo);
    }

    @Test
    public void A_addMessage(){
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .post("/api/rest/creds/dialog/" + userTwo.getId())
                .then().statusCode(201);
    }

    @Test
    public void B_getMessage() throws JsonProcessingException {
        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/dialog/" + userTwo.getId() + "/" + getLastMessage().getId())
                .then().assertThat()
                .body("text", equalTo(MESSAGE_TEXT));
    }

    @Test
    public void C_getChat() throws JsonProcessingException {
        ResponseList<Message> allDialog = getMessages();
        assertEquals(allDialog.getList().size(), allDialog.getTotal().intValue());
    }

    @Test
    public void D_changeMessage() throws JsonProcessingException {
        Integer messageId = getLastMessage().getId();
        Integer userId = userTwo.getId();

        testMessage.setText(NEW_MESSAGE_TEXT);
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/dialog/" + userId + "/" + messageId)
                .then().assertThat()
                .statusCode(200);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/dialog/" + userId + "/" + messageId)
                .then().assertThat()
                .statusCode(200)
                .body("text", equalTo(NEW_MESSAGE_TEXT));
    }

    @Test
    public void E_deleteMessage() throws JsonProcessingException {
        Integer messageId = getLastMessage().getId();
        Integer userId = userTwo.getId();
        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .delete("/api/rest/creds/dialog/" + userId + "/" + messageId)
                .then().assertThat()
                .statusCode(204);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/dialog/" + userId + "/" + messageId)
                .then().assertThat()
                .statusCode(404);
    }

    private ResponseList<Message> getMessages() throws JsonProcessingException {
        Response response = given()
                                    .header("token", USER_TOKEN)
                                    .queryParams(params)
                                    .get("/api/rest/creds/dialog/" + userTwo.getId());
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        return  objectMapper.readValue(json, new TypeReference<ResponseList<Message>>() {});
    }

    private Message getLastMessage() throws JsonProcessingException {
        ResponseList<Message> allDialog = getMessages();
        assertEquals(allDialog.getList().size(), allDialog.getTotal().intValue());
        return allDialog.getList().get(allDialog.getList().size()-1);
    }
}

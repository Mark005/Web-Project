package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.wrappers.ResponseList;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatServletTest extends AbstractServletTest{
    private User user;
    private static Integer testMessageId;
    private Message testMessage;

    @Before
    public void loadValues() throws JsonProcessingException {
        user = getUserByToken(USER_TOKEN);
        testMessage = new Message("tytyhrJKHNjnjnjNUNJn", user, null);
    }

    @Test
    public void A_addMessage(){
        testMessageId = given()
                                .header("token", USER_TOKEN)
                                .body(testMessage)
                                .post("/api/rest/creds/chat")
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_CREATED)
                                .body("id", notNullValue())
                                .body("text", equalTo(testMessage.getText()))
                                .body("userFrom.id", equalTo(user.getId()))
                                .body("userTo.id", nullValue())
                                .extract()
                                .path("id");
    }

    @Test
    public void B_getChat() throws JsonProcessingException {
        String json  = given()
                                .header("token", USER_TOKEN)
                                .queryParams(PAGINATION_PARAMS)
                                .get("/api/rest/creds/chat")
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_OK)
                                .extract()
                                .asString();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseList<Message> allChat = objectMapper.readValue(json, new TypeReference<ResponseList<Message>>() {});
        assertEquals(allChat.getList().size(), allChat.getTotal().intValue());
    }

    @Test
    public void C_getMessage() {
        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("text", equalTo(testMessage.getText()))
                .body("id", notNullValue())
                .body("text", equalTo(testMessage.getText()))
                .body("userFrom.id", equalTo(user.getId()))
                .body("userTo.id", nullValue());
    }

    @Test
    public void D_changeMessage() {
        testMessage.setText("pe_lkm5b-MK0bthm");
        given()
                .header("token", ANOTHER_USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("text", equalTo(testMessage.getText()))
                .body("id", notNullValue())
                .body("text", equalTo(testMessage.getText()))
                .body("userFrom.id", equalTo(user.getId()))
                .body("userTo.id", nullValue());
    }

    @Test
    public void E_deleteMessage() {
        given()
                .header("token", ANOTHER_USER_TOKEN)
                .delete("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/chat/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

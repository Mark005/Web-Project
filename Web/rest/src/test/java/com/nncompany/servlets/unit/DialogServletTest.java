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
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DialogServletTest extends AbstractServletTest {
    private User userOne;
    private User userTwo;
    private static Integer testMessageId;
    private Message testMessage;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void loadValues() throws JsonProcessingException {
        userOne = getUserByToken(USER_TOKEN);
        userTwo = getUserByToken(ANOTHER_USER_TOKEN);
        testMessage = new Message("tytyhrJKHNjnjnjNUNJn", userOne, userTwo);
    }

    @Test
    public void A_addMessage() {
        testMessageId = given()
                                .header("token", USER_TOKEN)
                                .body(testMessage)
                                .post("/api/rest/creds/dialog/" + userTwo.getId())
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_CREATED)
                                .body("id", notNullValue())
                                .body("text", equalTo(testMessage.getText()))
                                .body("userFrom.id", equalTo(userOne.getId()))
                                .body("userTo.id", equalTo(userTwo.getId()))
                                .extract()
                                .path("id");
    }

    @Test
    public void B_getMessage() {
        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("text", equalTo(testMessage.getText()));
    }

    @Test
    public void C_getChat() throws JsonProcessingException {
        String json = given()
                                .header("token", USER_TOKEN)
                                .queryParams(PAGINATION_PARAMS)
                                .get("/api/rest/creds/dialog/" + userTwo.getId())
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_OK)
                                .extract()
                                .asString();
        ResponseList<Message> allDialog = objectMapper.readValue(json, new TypeReference<ResponseList<Message>>() {});
        assertEquals(allDialog.getList().size(), allDialog.getTotal().intValue());
    }

    @Test
    public void D_changeMessage() {
        testMessage.setText("pe_lkm5b-MK0bthm");
        given()
                .header("token", ANOTHER_USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", USER_TOKEN)
                .body(testMessage)
                .patch("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("text", equalTo(testMessage.getText()))
                .body("userFrom.id", equalTo(userOne.getId()))
                .body("userTo.id", equalTo(userTwo.getId()));
    }

    @Test
    public void E_deleteMessage() {
        given()
                .header("token", ANOTHER_USER_TOKEN)
                .delete("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", USER_TOKEN)
                .get("/api/rest/creds/dialog/" + userTwo.getId() + "/" + testMessageId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

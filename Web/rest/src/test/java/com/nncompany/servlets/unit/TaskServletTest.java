package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskStatus;
import com.nncompany.api.model.enums.TaskType;
import com.nncompany.api.model.wrappers.ResponseList;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskServletTest extends AbstractServletTest {
    private static Integer taskId;
    private Task task;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void loadValues() throws JsonProcessingException {
        User admin = getUserByToken(ADMIN_TOKEN);
        User user = getUserByToken(USER_TOKEN);
        Calendar c = new GregorianCalendar(2000, 5, 5);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));

        task = new Task();
            task.setCreator(admin);
            task.setExecutor(user);
            task.setName("NlhjHJbhlHbhHBHhbyonhvt");
            task.setType(TaskType.PERSONAL);
            task.setSatus(TaskStatus.OPEN);
            task.setDeadline(c.getTime());
    }

    @Test
    public void A_addTask() throws JsonProcessingException {

        given()
                .header("token", USER_TOKEN)
                .body(task)
                .post("/api/rest/creds/tasks")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        taskId = given()
                        .header("token", ADMIN_TOKEN)
                        .body(task)
                        .post("/api/rest/creds/tasks")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("name", equalTo(task.getName()))
                        .body("type", equalTo(task.getType().name()))
                        .body("status", equalTo(task.getStatus().name()))
                        .body("deadline", equalTo(task.getDeadline().getTime()))
                        .body("creator.id", equalTo(task.getCreator().getId()))
                        .body("executor.id", equalTo(task.getExecutor().getId()))
                        .extract()
                        .path("id");
    }

    @Test
    public void B_getTasks() throws JsonProcessingException {

        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.OPEN).getType(), TaskType.PERSONAL);
        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.OPEN).getType(), TaskType.ADJUSTMENT);
        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.OPEN).getType(), TaskType.ELECTRONIC);
        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.OPEN).getType(), TaskType.WELDING);

        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.EXECUTING).getType(), TaskType.PERSONAL);
        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.EXECUTING).getType(), TaskType.ADJUSTMENT);
        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.EXECUTING).getType(), TaskType.ELECTRONIC);
        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.EXECUTING).getType(), TaskType.WELDING);

        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.CLOSE).getType(), TaskType.PERSONAL);
        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.CLOSE).getType(), TaskType.ADJUSTMENT);
        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.CLOSE).getType(), TaskType.ELECTRONIC);
        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.CLOSE).getType(), TaskType.WELDING);

        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.OPEN).getStatus(), TaskStatus.OPEN);
        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.EXECUTING).getStatus(), TaskStatus.EXECUTING);
        assertEquals(getTaskByTypeAndStatus(TaskType.PERSONAL, TaskStatus.CLOSE).getStatus(), TaskStatus.CLOSE);

        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.OPEN).getStatus(), TaskStatus.OPEN);
        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.EXECUTING).getStatus(), TaskStatus.EXECUTING);
        assertEquals(getTaskByTypeAndStatus(TaskType.ADJUSTMENT, TaskStatus.CLOSE).getStatus(), TaskStatus.CLOSE);

        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.OPEN).getStatus(), TaskStatus.OPEN);
        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.EXECUTING).getStatus(), TaskStatus.EXECUTING);
        assertEquals(getTaskByTypeAndStatus(TaskType.WELDING, TaskStatus.CLOSE).getStatus(), TaskStatus.CLOSE);

        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.OPEN).getStatus(), TaskStatus.OPEN);
        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.EXECUTING).getStatus(), TaskStatus.EXECUTING);
        assertEquals(getTaskByTypeAndStatus(TaskType.ELECTRONIC, TaskStatus.CLOSE).getStatus(), TaskStatus.CLOSE);
    }

    @Test
    public void C_changeTask(){
        task.setSatus(TaskStatus.CLOSE);

        given()
                .header("token", ADMIN_TOKEN)
                .body(task)
                .patch("/api/rest/creds/tasks/" + taskId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(task.getName()))
                .body("type", equalTo(task.getType().name()))
                .body("status", equalTo(task.getStatus().name()))
                .body("deadline", equalTo(task.getDeadline().getTime()))
                .body("creator.id", equalTo(task.getCreator().getId()))
                .body("executor.id", equalTo(task.getExecutor().getId()));
    }

    @Test
    public void D_deleteTask(){
        given()
                .header("token", USER_TOKEN)
                .delete("/api/rest/creds/tasks/" + taskId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

        given()
                .header("token", ADMIN_TOKEN)
                .delete("/api/rest/creds/tasks/" + taskId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .header("token", ADMIN_TOKEN)
                .get("/api/rest/creds/tasks/" + taskId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private Task getTaskByTypeAndStatus(TaskType type, TaskStatus status) throws JsonProcessingException {
        String json = given()
                                .header("token", ADMIN_TOKEN)
                                .queryParams(PAGINATION_PARAMS)
                                .queryParam("type", type)
                                .queryParam("status", status)
                                .get("/api/rest/creds/tasks")
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.SC_OK)
                                .extract()
                                .asString();
        ResponseList<Task> list = objectMapper.readValue(json, new TypeReference<ResponseList<Task>>() {});
        return list.getList().get(0);
    }
}

package com.nncompany.servlets.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskStatus;
import com.nncompany.api.model.enums.TaskType;
import com.nncompany.api.model.wrappers.ResponseList;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskServletTest extends AbstractServletTest {
    private User admin;
    private User user;
    private static Integer taskId;
    private Task task;
    private final String NAME = "NlhjHJbhlHbhHBHhbyonhvt";
    private final TaskType TYPE = TaskType.PERSONAL;
    private final TaskStatus STATUS = TaskStatus.OPEN;
    private final TaskStatus NEW_STATUS = TaskStatus.CLOSE;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void loadValues() throws JsonProcessingException {
        admin = getUserByToken(ADMIN_TOKEN);
        user = getUserByToken(USER_TOKEN);

        task = new Task();
            task.setCreator(admin);
            task.setExecutor(user);
            task.setName(NAME);
            task.setType(TYPE);
            task.setSatus(STATUS);
            task.setDeadline(new Date());
    }

    @Test
    public void A_addTask() throws JsonProcessingException {
        given()
                .header("token", ADMIN_TOKEN)
                .body(task)
                .post("/api/rest/creds/tasks")
                .then().statusCode(201);
    }

    @Test
    public void B_getTasks() throws JsonProcessingException {
        Response response = given()
                                    .header("token", ADMIN_TOKEN)
                                    .queryParams(PAGINATION_PARAMS)
                                    .queryParam("status", STATUS)
                                    .queryParam("type", TYPE)
                                    .get("/api/rest/creds/tasks");
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        ResponseList<Task> list = objectMapper.readValue(json, new TypeReference<ResponseList<Task>>() {});
        Task lastTask = list.getList().get(list.getTotal()-1);
        taskId = lastTask.getId();
        assertEquals(list.getTotal().intValue(), list.getList().size());
        assertEquals(lastTask.getName(), NAME);
        assertEquals(lastTask.getType(), TYPE);
        assertEquals(lastTask.getStatus(), STATUS);

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
        task.setSatus(NEW_STATUS);
        given()
                .header("token", ADMIN_TOKEN)
                .body(task)
                .patch("/api/rest/creds/tasks/" + taskId)
                .then().statusCode(200);

        given()
                .header("token", ADMIN_TOKEN)
                .get("/api/rest/creds/tasks/" + taskId)
                .then().statusCode(200)
                .body("status", equalTo(NEW_STATUS.name()));
    }

    @Test
    public void D_deleteTask(){
        given()
                .header("token", ADMIN_TOKEN)
                .delete("/api/rest/creds/tasks/" + taskId)
                .then().statusCode(204);
    }

    private Task getTaskByTypeAndStatus(TaskType type, TaskStatus status) throws JsonProcessingException {
        Response response = given()
                                    .header("token", ADMIN_TOKEN)
                                    .queryParams(PAGINATION_PARAMS)
                                    .queryParam("type", type)
                                    .queryParam("status", status)
                                    .get("/api/rest/creds/tasks");
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        String json = response.asString();
        ResponseList<Task> list = objectMapper.readValue(json, new TypeReference<ResponseList<Task>>() {});
        return list.getList().get(0);
    }
}

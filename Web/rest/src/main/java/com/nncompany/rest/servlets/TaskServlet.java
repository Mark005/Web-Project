package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.ITaskService;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;
import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.api.model.wrappers.ResponseList;
import com.nncompany.impl.util.UserKeeper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class TaskServlet {

    @Autowired
    ITaskService taskService;

    @ApiOperation(value = "Get all accessible tasks by type and status with with pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tasks received successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid query params", response = RequestError.class),
    })
    @GetMapping("/tasks")
    public ResponseEntity<Object> getAllTasks(@RequestParam Integer page,
                                              @RequestParam Integer pageSize,
                                              @RequestParam TaskType type,
                                              @RequestParam TaskSatus status){
        User loggUser = UserKeeper.getLoggedUser();
        ResponseList responseList;
        if(loggUser.isAdmin() || (!loggUser.isAdmin() && !type.equals(TaskType.PERSONAL))){
            responseList = new ResponseList(taskService.getAll(page, pageSize, status, type),
                                            taskService.getTotalCountForGetAll(status, type));
        } else {
            responseList = new ResponseList(taskService.getUsersTasks(loggUser, status, type),
                                            taskService.getTotalCountForGetUsersTasks(loggUser, status, type));
        }
        return ResponseEntity.ok(responseList);
    }


    @ApiOperation(value = "Add new task")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tasks created successfully"),
            @ApiResponse(code = 400, message = "Invalid task's json, check models for more info", response = RequestError.class),
            @ApiResponse(code = 403, message = "Access denied, only admin can add tasks", response = RequestError.class),
    })
    @PostMapping("/tasks")
    public ResponseEntity addTask(@RequestBody Task task){
        task.setCreator(UserKeeper.getLoggedUser());
        task.setSatus(TaskSatus.OPEN);
        taskService.save(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ApiOperation(value = "Change task's status")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task's status changed successfully"),
            @ApiResponse(code = 400, message = "Invalid path variable or task json, check models for more info", response = RequestError.class),
            @ApiResponse(code = 404, message = "Current task not found", response = RequestError.class),
    })
    @PatchMapping("/tasks/{id}")
    public ResponseEntity changeTask(@PathVariable Integer id,
                                     @RequestBody Task task){
        Task dbTask = taskService.get(id);
        if(dbTask == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "target task not found",
                                                        "task deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        dbTask.setSatus(task.getSatus());
        taskService.update(dbTask);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Task deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Access denied, only admin can delete tasks", response = RequestError.class),
            @ApiResponse(code = 404, message = "Current task not found", response = RequestError.class),
    })
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable Integer id){
        Task dbTask = taskService.get(id);
        if(dbTask == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "target task not found",
                                                        "task deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        taskService.delete(dbTask);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

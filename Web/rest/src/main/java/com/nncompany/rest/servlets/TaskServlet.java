package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.ITaskService;
import com.nncompany.api.model.entities.Task;
import com.nncompany.api.model.enums.TaskSatus;
import com.nncompany.api.model.enums.TaskType;
import com.nncompany.impl.util.UserKeeper;
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

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllPersonalTasks(@RequestParam TaskType type,
                                                          @RequestParam TaskSatus status){
        if(UserKeeper.getLoggedUser().isAdmin()){
            return ResponseEntity.ok(taskService.getTasks(status, type));
        } else {
            return ResponseEntity.ok(taskService.getUsersTasks(UserKeeper.getLoggedUser(), status, type));
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity addTask(@RequestBody Task task){
        task.setCreator(UserKeeper.getLoggedUser());
        task.setSatus(TaskSatus.OPEN);
        taskService.save(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/tasks")
    public ResponseEntity changeTask(@RequestBody Task task){
        Task dbTask = taskService.get(task.getId());
        if(dbTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dbTask.setSatus(task.getSatus());
        taskService.update(dbTask);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tasks")
    public ResponseEntity deleteTask(@RequestBody Task task){
        if(taskService.get(task.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskService.delete(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

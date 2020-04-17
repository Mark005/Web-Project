package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.User;
import com.nncompany.impl.util.UserKeeper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class UserServlet {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Get current logged user")
    @GetMapping("/user")
    public ResponseEntity<User> getLoggedUser(){
        return ResponseEntity.ok(UserKeeper.getLoggedUser());
    }

    @ApiOperation(value = "Get users with pagination, count elements returned in header 'X-Total-Count' ")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam Integer offset,
                                               @RequestParam Integer limit){
        if(offset>= 0 && limit>= 0) {
            List<User> users = userService.getWithPagination(offset, limit);
            if(users != null) {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("X-Total-Count", users.size()+"");
                return ResponseEntity.ok().headers(responseHeaders).body(users);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Get user by id")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer userId){
        User user = userService.get(userId);
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(value = "Update user by id except admin status " +
            "(only admin can update someone else except itself, " +
            "and only admin can change admin status)")
    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Integer userId,
                                     @RequestBody User requestUser){
        User targetUser = userService.get(userId);
        User loggedUser = UserKeeper.getLoggedUser();
        if(targetUser != null) {
            if(loggedUser.isAdmin()) {
                requestUser.setId(userId);
                userService.update(requestUser);
                return new ResponseEntity<>(HttpStatus.OK);
            } else if(targetUser.getId().equals(loggedUser.getId())){
                requestUser.setId(userId);
                requestUser.setAdmin(loggedUser.isAdmin());
                userService.update(requestUser);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Delete user by id (only admin can delete someone else except itself)")
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Integer userId){
        User targetUser = userService.get(userId);
        User loggedUser = UserKeeper.getLoggedUser();
        if(targetUser != null) {
            if(targetUser.getId().equals(loggedUser.getId()) ||
                    loggedUser.isAdmin()) {
                userService.delete(targetUser);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

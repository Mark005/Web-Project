package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.User;
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
public class UserServlet {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Get current logged user")
    @ApiResponse(code = 200, message = "User returned successfully", response = User.class)
    @GetMapping("/user")
    public ResponseEntity<User> getLoggedUser(){
        return ResponseEntity.ok(UserKeeper.getLoggedUser());
    }

    @ApiOperation(value = "Get users with pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users returned successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = RequestError.class)
    })
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(@RequestParam Integer page,
                                           @RequestParam Integer pageSize){
        return ResponseEntity.ok(new ResponseList(userService.getWithPagination(page, pageSize),
                                                  userService.getTotalCount()));
    }

    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned successfully", response = User.class),
            @ApiResponse(code = 400, message = "Invalid path variables", response = RequestError.class),
            @ApiResponse(code = 404, message = "User with current id not found", response = RequestError.class)
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id){
        User user = userService.get(id);
        if(user == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Get user by (name/surname/certificate number)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users returned successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = RequestError.class),
            @ApiResponse(code = 404, message = "Users with this criteria not found", response = RequestError.class)
    })
    @GetMapping("/users/search")
    public ResponseEntity<Object> findUsers(@RequestParam Integer page,
                                            @RequestParam Integer pageSize,
                                            @RequestParam String searchString){
        List<User> users = userService.search(searchString);
        if(users == null || users.size() == 0) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "users not found",
                                                        "users with this criteria not found"),
                                                        HttpStatus.NOT_FOUND);
        }
        ResponseList<User> responseList = new ResponseList();
        Integer firstItem = page*pageSize;
        Integer lastItem = page*pageSize+pageSize;
        responseList.setList(users.subList(firstItem > users.size() ? users.size() : firstItem,
                                           lastItem  > users.size() ? users.size() : lastItem));
        responseList.setTotal(users.size());
        return ResponseEntity.ok(responseList);
    }



    @ApiOperation(value = "Update user by id except admin status " +
                          "(only admin can update someone else except itself, " +
                          "and only admin can change admin status)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User changed successfully"),
            @ApiResponse(code = 400, message = "Invalid path variables", response = RequestError.class),
            @ApiResponse(code = 403, message = "You have hot permission to change this user", response = RequestError.class),
            @ApiResponse(code = 404, message = "User with current id not found", response = RequestError.class)
    })
    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id,
                                     @RequestBody User requestUser){
        User loggedUser = UserKeeper.getLoggedUser();
        User targetUser = userService.get(id);
        if(targetUser == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(loggedUser.isAdmin()) {
            requestUser.setId(id);
            userService.update(requestUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if(targetUser.equals(loggedUser)){
            requestUser.setId(id);
            requestUser.setAdmin(loggedUser.isAdmin());
            userService.update(requestUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied",
                                                        "you have hot permission to change this user"),
                                                        HttpStatus.FORBIDDEN);
        }
    }

    @ApiOperation(value = "Delete user by id (only admin can delete someone else except itself)")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid path variables", response = RequestError.class),
            @ApiResponse(code = 403, message = "You have hot permission to delete this user", response = RequestError.class),
            @ApiResponse(code = 404, message = "User with current id not found", response = RequestError.class)
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        User targetUser = userService.get(id);
        User loggedUser = UserKeeper.getLoggedUser();
        if(targetUser == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(targetUser.equals(loggedUser) ||  loggedUser.isAdmin()) {
            userService.delete(targetUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied",
                                                        "you have hot permission to delete this user"),
                                                        HttpStatus.FORBIDDEN);
        }
    }
}

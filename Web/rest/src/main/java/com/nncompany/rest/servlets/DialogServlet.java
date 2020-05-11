package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IMessageService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.api.model.wrappers.ResponseList;
import com.nncompany.impl.util.UserKeeper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class DialogServlet {

    @Autowired
    IMessageService messageService;

    @Autowired
    IUserService userService;

    @ApiOperation(value = "Get dialog with user by 'userId' with pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dialog received successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid path variable or query params", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target user not found", response = RequestError.class),
    })
    @GetMapping("/dialog/{userId}")
    public ResponseEntity<Object> getDialogWithUser(@PathVariable Integer userId,
                                                    @RequestParam Integer page,
                                                    @RequestParam Integer pageSize){
        User one = UserKeeper.getLoggedUser();
        User two = userService.get(userId);
        if(two == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "target user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        ResponseList responseList = new ResponseList<>(messageService.getDialogWithPagination(one, two, page, pageSize),
                                                       messageService.getTotalCountMessages(one,two));
        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "Get message by id from dialog with user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dialog received successfully", response = Message.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Current message from another dialog", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target user or message not found", response = RequestError.class),
    })
    @GetMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity<Object> getMessageFromDialogWithUser(@PathVariable Integer userId,
                                                               @PathVariable Integer msgId){
        User one = UserKeeper.getLoggedUser();
        User two = userService.get(userId);
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                         "message not found",
                                                         "message deleted or not created"),
                                                          HttpStatus.NOT_FOUND);
        } else if(two == null){
            return new ResponseEntity<>(new RequestError(404,
                                                         "target user not found",
                                                         "user deleted or not created"),
                                                          HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(one) && dbMessage.getUserTo().equals(two) ||
           dbMessage.getUserFrom().equals(two) && dbMessage.getUserTo().equals(one)) {
            return ResponseEntity.ok(dbMessage);
        } else {
            return new ResponseEntity<>(new RequestError(403,
                                                         "access denied to target message",
                                                         "requested message is not in the current dialog"),
                                                          HttpStatus.FORBIDDEN);
        }
    }

    @ApiOperation(value = "Send message to user by 'userId'")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Message sent successfully", response = Message.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target user not found", response = RequestError.class),
    })
    @PostMapping("/dialog/{userId}")
    public ResponseEntity sendDialogMessage(@PathVariable Integer userId,
                                            @RequestBody Message message){
        message.setUserFrom(UserKeeper.getLoggedUser());
        User userTo = userService.get(userId);
        if(userTo == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "target user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        message.setUserTo(userTo);
        message.setDate(new Date());
        messageService.save(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Change message's text by message id from dialog with user(Attention: user can change only his message and only message's text)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message text changed successfully", response = Message.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Current message from another dialog, or you are not creator", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target message not found", response = RequestError.class),
    })
    @PatchMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity changeDialogsMessage(@PathVariable Integer userId,
                                               @PathVariable Integer msgId,
                                               @RequestBody Message message ){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "target message not found",
                                                        "message deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
           dbMessage.getUserTo().equals(userService.get(userId))) {
            dbMessage.setText(message.getText());
            messageService.update(dbMessage);
            return new ResponseEntity<>(dbMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied to target message",
                                                        "requested message is not in the current dialog"),
                                                        HttpStatus.FORBIDDEN);
        }
    }

    @ApiOperation(value = "Delete message by message id from dialog with user(Attention: user can delete only his message)")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Message deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Current message from another dialog", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target message not found", response = RequestError.class),
    })
    @DeleteMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity deleteDialogsMessage(@PathVariable Integer userId,
                                               @PathVariable Integer msgId ){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "target message not found",
                                                        "message deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
                dbMessage.getUserTo().equals(userService.get(userId))) {
            messageService.delete(dbMessage);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied to target message",
                                                        "requested message is not in the current dialog"),
                                                        HttpStatus.FORBIDDEN);
        }
    }
}

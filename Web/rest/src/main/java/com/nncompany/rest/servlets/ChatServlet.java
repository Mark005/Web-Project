package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IMessageService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Message;
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

import java.util.Date;

@RestController
@RequestMapping("/api/rest/creds")
public class ChatServlet {

    @Autowired
    IMessageService messageService;

    @Autowired
    IUserService userService;


    @ApiOperation(value = "Get chat message with pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat messages received successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid query params", response = RequestError.class),
    })
    @GetMapping("/chat")
    public ResponseEntity<Object> getChatsMessages(@RequestParam Integer page,
                                                   @RequestParam Integer pageSize){
        if(page < 0 || pageSize < 1) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "query params error",
                                                        "query params must be: page >= 0 and pageSize > 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(messageService.getChatWithPagination(page, pageSize));
    }

    @ApiOperation(value = "Get chat's message by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat message received successfully", response = Message.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Message with current id from private dialog", response = RequestError.class),
            @ApiResponse(code = 404, message = "Message not found", response = RequestError.class)
    })
    @GetMapping("/chat/{msgId}")
    public ResponseEntity<Object> getChatsMessage(@PathVariable Integer msgId){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "message not found",
                                                        "message with current id is deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserTo() != null){
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied for this message",
                                                        "message with current id not from common chat"),
                                                        HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(dbMessage);
    }

    @ApiOperation(value = "Send message to common chat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Message sent successfully"),
            @ApiResponse(code = 400, message = "Invalid message json, for more info check models")
    })
    @PostMapping("/chat")
    public ResponseEntity sendMessageToChat(@RequestBody Message message){
        message.setUserFrom(UserKeeper.getLoggedUser());
        message.setDate(new Date());
        messageService.save(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Change chat's message text")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message changed successfully"),
            @ApiResponse(code = 400, message = "Invalid message json(check models), or path variable(must be > 0)", response = RequestError.class),
            @ApiResponse(code = 403, message = "Message with current id from private dialog", response = RequestError.class),
            @ApiResponse(code = 404, message = "Message not found", response = RequestError.class)
    })
    @PatchMapping("/chat/{msgId}")
    public ResponseEntity changeChatsMessage(@PathVariable Integer msgId,
                                             @RequestBody Message message){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "message not found",
                                                        "message with current id is deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(!dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
            dbMessage.getUserTo() != null) {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied for this message",
                                                        "message with current id not from common chat"),
                                                        HttpStatus.FORBIDDEN);
        }
        dbMessage.setText(message.getText());
        messageService.update(dbMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete chat's message text")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Message deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid message json(check models), or path variable(must be > 0)", response = RequestError.class),
            @ApiResponse(code = 403, message = "Message with current id from private dialog", response = RequestError.class),
            @ApiResponse(code = 404, message = "Message not found", response = RequestError.class)
    })
    @DeleteMapping("/chat/{msgId}")
    public ResponseEntity deleteChatsMessage(@PathVariable Integer msgId){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "message not found",
                                                        "message with current id is deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
           dbMessage.getUserTo() == null) {
            return new ResponseEntity<>(new RequestError(403,
                                                        "access denied for this message",
                                                        "message with current id not from common chat"),
                                                        HttpStatus.FORBIDDEN);
        }
        messageService.delete(dbMessage);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

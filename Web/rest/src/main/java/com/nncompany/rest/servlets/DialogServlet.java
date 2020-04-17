package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IMessageService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Message;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.impl.util.UserKeeper;
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

    @GetMapping("/dialog/{userId}")
    public ResponseEntity<Object> getDialogWithUser(@PathVariable Integer userId,
                                                           @RequestParam Integer offset,
                                                           @RequestParam Integer limit){
        User one = UserKeeper.getLoggedUser();
        User two = userService.get(userId);
        if(two == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "target user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        if(offset < 0 || limit < 0){
            return new ResponseEntity<>(new RequestError(400,
                                                        "request param error",
                                                        "request params must be numbers >= 0"),
                                                        HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(messageService.getDialogWithPagination(one, two, offset, limit));
    }

    @GetMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity<Object> getDialogWithUser(@PathVariable Integer userId,
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
            return new ResponseEntity<>(new RequestError(404,
                                                         "target user not found",
                                                         "requested message is not in the current dialog"),
                                                          HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dialog/{userId}")
    public ResponseEntity sendDialogMessage(@PathVariable Integer userId,
                                            @RequestBody Message message){
        message.setUserFrom(UserKeeper.getLoggedUser());
        message.setUserTo(userService.get(userId));
        message.setDate(new Date());
        messageService.save(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity changeDialogsMessage(@PathVariable Integer userId,
                                               @PathVariable Integer msgId,
                                               @RequestBody Message message ){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
           dbMessage.getUserTo().equals(userService.get(userId))) {
            dbMessage.setText(message.getText());
            messageService.update(dbMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/dialog/{userId}/{msgId}")
    public ResponseEntity deleteDialogsMessage(@PathVariable Integer userId,
                                               @PathVariable Integer msgId ){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
                dbMessage.getUserTo().equals(userService.get(userId))) {
            messageService.delete(dbMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}

package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IMessageService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Message;
import com.nncompany.impl.util.UserKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class ChatServlet {

    @Autowired
    IMessageService messageService;

    @Autowired
    IUserService userService;

    @GetMapping("/chat")
    public ResponseEntity<List<Message>> getChatsMessages(@RequestParam Integer offset,
                                                          @RequestParam Integer limit){
        return ResponseEntity.ok(messageService.getChatWithPagination(offset, limit));
    }

    @GetMapping("/chat/{msgId}")
    public ResponseEntity<Message> getChatsMessage(@PathVariable Integer msgId){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserTo() != null){
            return ResponseEntity.ok(dbMessage);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/chat")
    public ResponseEntity sendMessageToChat(@RequestBody Message message){
        message.setUserFrom(UserKeeper.getLoggedUser());
        message.setDate(new Date());
        messageService.save(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/chat/{msgId}")
    public ResponseEntity changeChatsMessage(@PathVariable Integer msgId,
                                             @RequestBody Message message){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser())&&
           dbMessage.getUserTo() == null) {
            dbMessage.setText(message.getText());
            messageService.update(dbMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/chat/{msgId}")
    public ResponseEntity deleteChatsMessage(@PathVariable Integer msgId){
        Message dbMessage = messageService.get(msgId);
        if(dbMessage == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dbMessage.getUserFrom().equals(UserKeeper.getLoggedUser()) &&
           dbMessage.getUserTo() == null) {
            messageService.delete(dbMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}

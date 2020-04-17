package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IBriefingService;
import com.nncompany.api.interfaces.services.IUserBriefingService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;
import com.nncompany.impl.util.UserKeeper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/creds/user-briefing")
public class UserBriefingServlet {

    @Autowired
    IUserBriefingService userBriefingService;

    @Autowired
    IBriefingService briefingService;

    @Autowired
    IUserService userService;

    @ApiOperation(value = "Get briefings by user id")
    @GetMapping("/briefings-by-user/{id}")
    public ResponseEntity<List<Briefing>> getBriefingsByCurrentUser(@PathVariable Integer id) {
        if(id<1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.get(id);
        if (user != null) {
            return ResponseEntity.ok(userBriefingService.getBriefingsByCurrentUser(user));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Get users by briefing id")
    @GetMapping("/users-by-briefing/{id}")
    public ResponseEntity<List<User>> getUsersByCurrentBriefing(@PathVariable Integer id) {
        if(id<1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Briefing briefing = briefingService.get(id);
        if (briefing != null) {
            return ResponseEntity.ok(userBriefingService.getUsersByCurrentBriefing(briefing));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Get all conducted briefings with sorting")
    @GetMapping("/briefings")
    public ResponseEntity<List<UserBriefing>> getAll(@RequestParam UserBriefingSort sort,
                                                     @RequestParam Direction direction) {
        if (sort != null && direction != null) {
            return ResponseEntity.ok(userBriefingService.getAll(sort, direction));
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //TODO add briefing to user
    @ApiOperation(value = "Add new briefing conduction")
    @PostMapping("/briefings")
    public ResponseEntity addConductedBriefing(@RequestBody UserBriefing userBriefing) {
        if(userService.get(userBriefing.getUser().getId()) == null ||
            briefingService.get(userBriefing.getId()) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userBriefingService.save(userBriefing);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO update conduction date
    @ApiOperation(value = "Update briefing conduction day")
    @PatchMapping("/briefings/{id}")
    public ResponseEntity updateConductionDate(@PathVariable Integer id,
                                               @RequestBody UserBriefing userBriefing) {
        UserBriefing dbUserBriefing = userBriefingService.get(id);
        if(dbUserBriefing != null){
            dbUserBriefing.setLastDate(userBriefing.getLastDate());
            userBriefingService.update(dbUserBriefing);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Delete briefing conduction")
    @DeleteMapping("/briefings/{id}")
    public ResponseEntity deleteConductedBriefing(@PathVariable Integer id) {
        UserBriefing userBriefing = userBriefingService.get(id);
        if(userBriefing == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userBriefingService.delete(userBriefing);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

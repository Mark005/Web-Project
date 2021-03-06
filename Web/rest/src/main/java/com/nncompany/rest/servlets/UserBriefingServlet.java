package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IBriefingService;
import com.nncompany.api.interfaces.services.IUserBriefingService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.api.model.entities.User;
import com.nncompany.api.model.entities.UserBriefing;
import com.nncompany.api.model.enums.Direction;
import com.nncompany.api.model.enums.UserBriefingSort;
import com.nncompany.api.model.wrappers.RequestError;
import com.nncompany.api.model.wrappers.ResponseList;
import com.nncompany.impl.service.UserBriefingService;
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
@RequestMapping("/api/rest/creds/user-briefing")
public class UserBriefingServlet {

    @Autowired
    IUserBriefingService userBriefingService;

    @Autowired
    IBriefingService briefingService;

    @Autowired
    IUserService userService;

    @ApiOperation(value = "Get user's briefings by user's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Briefings received successfully", response = Briefing[].class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target user not found", response = RequestError.class),
    })
    @GetMapping("/conducted-by-user/{id}")
    public ResponseEntity<Object> getBriefingsByCurrentUser(@PathVariable Integer id) {
        User user = userService.get(id);
        if (user == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "target user not found",
                                                        "user deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userBriefingService.getBriefingsByCurrentUser(user));
    }

    @ApiOperation(value = "Get users by briefing id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users received successfully", response = User[].class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 404, message = "Target briefing not found", response = RequestError.class),
    })
    @GetMapping("/conducted-by-briefing/{id}")
    public ResponseEntity<Object> getUsersByCurrentBriefing(@PathVariable Integer id) {
        Briefing briefing = briefingService.get(id);
        if (briefing == null) {
            return new ResponseEntity<>(new RequestError(404,
                                                        "target briefing not found",
                                                        "briefing deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userBriefingService.getUsersByCurrentBriefing(briefing));
    }

    @ApiOperation(value = "Get conducted briefing by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Received successfully", response = UserBriefing.class),
            @ApiResponse(code = 400, message = "Invalid path variable", response = RequestError.class),
            @ApiResponse(code = 404, message = "Current conduction not found", response = RequestError.class),
    })
    @GetMapping("/conducted/{id}")
    public ResponseEntity<Object> getById(@PathVariable Integer id) {
        UserBriefing userBriefing = userBriefingService.get(id);
        if(userBriefing == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "not found",
                                                        "Current conduction deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userBriefing);
    }

    @ApiOperation(value = "Get all conducted briefings with sorting")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users received successfully", response = ResponseList.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = RequestError.class),
    })
    @GetMapping("/conducted")
    public ResponseEntity<Object> getAll(@RequestParam Integer page,
                                         @RequestParam Integer pageSize,
                                         @RequestParam UserBriefingSort sort,
                                         @RequestParam Direction direction) {
        if (sort == null || direction == null) {
            return new ResponseEntity<>(new RequestError(400,
                                                        "invalid request params",
                                                        "one of request params(sort or direction) = null, " +
                                                                   "for more info check available params"),
                                                        HttpStatus.BAD_REQUEST);
        }
        ResponseList<UserBriefing> responseList = new ResponseList<>();
        responseList.setList(userBriefingService.getAll(page, pageSize, sort, direction));
        responseList.setTotal(userBriefingService.getTotalCount());
        return ResponseEntity.ok(responseList);
    }


    @ApiOperation(value = "Add new briefing conduction")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Briefing conducted successfully", response = UserBriefing.class),
            @ApiResponse(code = 400, message = "Invalid user-briefing json"),
            @ApiResponse(code = 403, message = "Access denied, only admins can conduct briefings", response = RequestError.class),
            @ApiResponse(code = 404, message = "User or briefing not found", response = RequestError.class),
    })
    @PostMapping("/conducted")
    public ResponseEntity addConductedBriefing(@RequestBody UserBriefing userBriefing) {
        User dbUser = userService.get(userBriefing.getUser().getId());
        Briefing dbBriefing = briefingService.get(userBriefing.getBriefing().getId());
        if(dbUser == null || dbBriefing == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "user or briefing not found",
                                                        "user or briefing deleted or not created"),
                                                        HttpStatus.NOT_FOUND);
        }
        userBriefing.setUser(dbUser);
        userBriefing.setBriefing(dbBriefing);
        userBriefingService.save(userBriefing);
        return new ResponseEntity<>(userBriefing, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Update briefing's conduction day")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Briefing's conduction day updated successfully", response = UserBriefing.class),
            @ApiResponse(code = 400, message = "Invalid user-briefing json or path variable", response = RequestError.class),
            @ApiResponse(code = 403, message = "Access denied, only admins can conduct briefings", response = RequestError.class),
            @ApiResponse(code = 404, message = "Briefing conduction not found", response = RequestError.class),
    })
    @PatchMapping("/conducted/{id}")
    public ResponseEntity updateConductionDate(@PathVariable Integer id,
                                               @RequestBody UserBriefing userBriefing) {
        UserBriefing dbUserBriefing = userBriefingService.get(id);
        if(dbUserBriefing == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "briefing conduction not found",
                                                        "briefing not conducted or conduction deleted"),
                                                        HttpStatus.NOT_FOUND);
        }
        dbUserBriefing.setLastDate(userBriefing.getLastDate());
        userBriefingService.update(dbUserBriefing);
        return new ResponseEntity<>(dbUserBriefing, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete briefing conduction")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Briefing conduction deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid user-briefing json or path variable"),
            @ApiResponse(code = 403, message = "Access denied, only admins can conduct briefings", response = RequestError.class),
            @ApiResponse(code = 404, message = "Briefing conduction not found", response = RequestError.class),
    })
    @DeleteMapping("/conducted/{id}")
    public ResponseEntity deleteConductedBriefing(@PathVariable Integer id) {
        UserBriefing userBriefing = userBriefingService.get(id);
        if(userBriefing == null){
            return new ResponseEntity<>(new RequestError(404,
                                                        "briefing conduction not found",
                                                        "briefing not conducted or conduction deleted"),
                                                        HttpStatus.NOT_FOUND);
        }
        userBriefingService.delete(userBriefing);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

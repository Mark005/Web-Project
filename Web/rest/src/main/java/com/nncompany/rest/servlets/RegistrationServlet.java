package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IUserCredsService;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.UserCreds;
import com.nncompany.api.model.wrappers.BooleanResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/rest/registration")
public class RegistrationServlet {

    @Autowired
    private IUserCredsService userCredsService;

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Check if the login is busy")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Received response in json"),
            @ApiResponse(code = 400, message = "Invalid or lost login field")
    })
    @PostMapping("/checkLogin")
    public ResponseEntity<BooleanResponse> login(@RequestBody UserCreds requestUserCreds) {
        if(requestUserCreds.getLogin() == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userCredsService.checkLogin(requestUserCreds.getLogin())) {
            return ResponseEntity.ok(new BooleanResponse(true));
        } else {
            return ResponseEntity.ok(new BooleanResponse(false));
        }
    }

    @ApiOperation(value = "Registration new user")
    @PostMapping("/user")
    public ResponseEntity registration(@RequestBody UserCreds requestUserCreds) {
        userService.save(requestUserCreds.getUser());
        userCredsService.save(requestUserCreds);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

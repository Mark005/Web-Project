package com.nncompany.rest.servlets;

import com.google.gson.JsonObject;
import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.services.IUserCredsService;
import com.nncompany.api.model.wrappers.Token;
import com.nncompany.api.model.entities.UserCreds;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/logIn")
public class AutorizationServlet{
    @Autowired
    private IUserCredsService userCredsService;

    @Autowired
    private  ITokenHandler tokenHandler;


    @ApiOperation(value = "Login in system  (you can sand json only with login and pass)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid json, need add login and pass"),
            @ApiResponse(code = 404, message = "Can't find user with this login and password")
    })
    @PostMapping()
    public ResponseEntity<Token> login(@RequestBody UserCreds requestUserCreds){
        UserCreds userCreds = null;
        if(requestUserCreds.getLogin()!=null && requestUserCreds.getPass()!=null) {
            userCreds = userCredsService.
                    getUserCredsByLoginAndPass(requestUserCreds.getLogin(),
                                               requestUserCreds.getPass());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userCreds != null) {
            JsonObject userJsonObj = new JsonObject();
            userJsonObj.addProperty("id", userCreds.getUser().getId());
            String json = userJsonObj.toString();
            return ResponseEntity.ok(new Token(tokenHandler.getToken(json)));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

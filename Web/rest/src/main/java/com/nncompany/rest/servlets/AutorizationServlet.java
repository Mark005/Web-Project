package com.nncompany.rest.servlets;

import com.google.gson.JsonObject;
import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.IUserCredsService;
import com.nncompany.api.model.Token;
import com.nncompany.api.model.UserCreds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/logIn")
public class AutorizationServlet{
    @Autowired
    private IUserCredsService userCredsService;

    @Autowired
    private  ITokenHandler tokenHandler;

    @PostMapping()
    public ResponseEntity<Token> login(@RequestBody UserCreds requestUserCreds){
        UserCreds userCreds = null;
        if(requestUserCreds.getLogin()!=null && requestUserCreds.getPass()!=null) {
            userCreds = userCredsService.getUserCredsByLogin(requestUserCreds.getLogin(), requestUserCreds.getPass());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userCreds != null) {
            JsonObject userJsonObj = new JsonObject();
            userJsonObj.addProperty("id", userCreds.getUser().getId());
            String json = userJsonObj.toString();
            return ResponseEntity.ok(new Token(tokenHandler.getToken(json)));
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

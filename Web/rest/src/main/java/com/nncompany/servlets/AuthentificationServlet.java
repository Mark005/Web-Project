package com.nncompany.servlets;

import com.nncompany.api.model.User;
import com.nncompany.impl.util.UserKeeper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creds/user")
public class AuthentificationServlet{

    @PostMapping()
    protected ResponseEntity<User> checkUser(){
        return ResponseEntity.ok(UserKeeper.getLoggedUser());
    }
}

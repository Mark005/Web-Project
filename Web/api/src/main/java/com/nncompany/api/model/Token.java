package com.nncompany.api.model;

public class Token {
    String token;

    public Token(String tokenBody){
        this.token = tokenBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

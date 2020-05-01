package com.nncompany.api.model.wrappers;

public class Token {

    private String token;

    public Token(){}

    public Token(String tokenBody){
        this.token = tokenBody;
    }

    public String getValue() {
        return token;
    }

    public void setValue(String token) {
        this.token = token;
    }
}

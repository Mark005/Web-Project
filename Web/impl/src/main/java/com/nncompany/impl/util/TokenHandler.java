package com.nncompany.impl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.interfaces.ITokenHandler;

import com.nncompany.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component()
public class TokenHandler implements ITokenHandler {

    private Key key;
    ObjectMapper mapper = new ObjectMapper();

    public TokenHandler(){
        System.out.println("initttt");
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Override
    public void regenerateKey() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Override
    public String getToken(String json){
        return Jwts.builder().setSubject(json).signWith(key).compact();
    }

    @Override
    public boolean checkToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public User getUserFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody();
        String json =  claims.getSubject();
        User user = null;
        try {
            user = mapper.readValue(json, User.class);
        } catch (Exception e){
            return null;
        }
        return user;
    }
}

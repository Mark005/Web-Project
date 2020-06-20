package com.nncompany.impl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.interfaces.IKeyGenerator;
import com.nncompany.api.interfaces.ITokenHandler;

import com.nncompany.api.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component()
public class TokenHandler implements ITokenHandler {

    private Key key;

    @Autowired
    public TokenHandler(IKeyGenerator keyGenerator){
        key = keyGenerator.generateKey();
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
        if(token !=null && !token.equals("")) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                return false;
            }
        } else return false;
    }

    @Override
    public Integer getUserIdFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        String userId =  claims.getSubject();
        try {
            return Integer.parseInt(userId);
        } catch (Exception e){
            return null;
        }
    }
}

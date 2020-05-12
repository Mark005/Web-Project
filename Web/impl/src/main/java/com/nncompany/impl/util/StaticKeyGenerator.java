package com.nncompany.impl.util;

import com.nncompany.api.interfaces.IKeyGenerator;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@Profile("staticKey")
public class StaticKeyGenerator implements IKeyGenerator {
    @Override
    public Key generateKey() {
        System.out.println("static key");
        return Keys.hmacShaKeyFor(new byte[256]);
    }
}
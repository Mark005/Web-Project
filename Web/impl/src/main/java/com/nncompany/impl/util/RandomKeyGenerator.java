package com.nncompany.impl.util;

import com.nncompany.api.interfaces.IKeyGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@Profile("randomKey")
public class RandomKeyGenerator implements IKeyGenerator {
    @Override
    public Key generateKey() {
        System.out.println("random key");
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}

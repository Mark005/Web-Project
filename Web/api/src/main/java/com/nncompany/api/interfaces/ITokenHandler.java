package com.nncompany.api.interfaces;

import com.nncompany.api.model.entities.User;

public interface ITokenHandler {

    void regenerateKey();

    String getToken(String json);

    boolean checkToken(String token);

    User getUserFromToken(String token);
}

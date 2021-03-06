package com.nncompany.rest.filters;

import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.services.IUserService;
import com.nncompany.api.model.entities.User;
import com.nncompany.impl.util.UserKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends HandlerInterceptorAdapter {
    @Autowired
    ITokenHandler tokenHandler;
    @Autowired
    IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler){
        String token = request.getHeader("token");
        if (tokenHandler.checkToken(token)) {
            User dbUser = userService.get(tokenHandler.getUserIdFromToken(token));
            UserKeeper.setLoggedUser(dbUser);
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}

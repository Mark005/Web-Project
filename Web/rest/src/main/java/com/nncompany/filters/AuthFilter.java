package com.nncompany.filters;

import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.IUserService;
import com.nncompany.api.model.User;
import com.nncompany.di.Di;
import util.UserKeeper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/creds/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        ITokenHandler tokenHandler = Di.getInstance().load(ITokenHandler.class);
        IUserService userService = Di.getInstance().load(IUserService.class);

        String token = httpServletRequest.getHeader("token");
        if (tokenHandler.checkToken(token)) {
            User inputUser = tokenHandler.getUserFromToken(token);
            User dbUser = userService.get(inputUser.getId());
            UserKeeper.setLoggedUser(dbUser);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}

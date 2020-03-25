package com.nncompany.filters;

import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.IUserService;
import com.nncompany.api.model.User;
import com.nncompany.impl.util.UserKeeper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthFilter implements Filter {
    ITokenHandler tokenHandler;
    IUserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

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
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.tokenHandler = ctx.getBean(ITokenHandler.class);
        this.userService = ctx.getBean(IUserService.class);
    }

    @Override
    public void destroy() {

    }
}

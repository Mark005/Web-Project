package filters;

import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.di.Di;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/cerds/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        ITokenHandler tokenHandler = Di.getInstance().load(ITokenHandler.class);
        String token = httpServletRequest.getHeader("token");
        if (tokenHandler.checkToken(token)) {
            httpServletResponse.setContentType("text/html");
            PrintWriter out = httpServletResponse.getWriter();
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

package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.model.User;
import com.nncompany.di.Di;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cerds/user")
public class Authentification extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ITokenHandler tokenHandler = Di.getInstance().load(ITokenHandler.class);
        String token = req.getHeader("token");
        User user = tokenHandler.getUserFromToken(token);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(mapper.writeValueAsString(user));
    }
}

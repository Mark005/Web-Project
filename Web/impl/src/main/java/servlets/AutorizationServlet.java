package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.IUserCredsService;
import com.nncompany.api.model.UserCreds;
import com.nncompany.di.Di;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logIn")
public class AutorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        IUserCredsService userCredsService = Di.getInstance().load(IUserCredsService.class);
        ITokenHandler tokenHandler = Di.getInstance().load(ITokenHandler.class);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        UserCreds requestUserCreds = null;
        try {
            requestUserCreds = mapper.readValue(jb.toString(), UserCreds.class);
        } catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        UserCreds userCreds = null;
        if(requestUserCreds.getLogin()!=null && requestUserCreds.getPass()!=null) {
            userCreds = userCredsService.getUserCredsByLogin(requestUserCreds.getLogin(), requestUserCreds.getPass());
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if(userCreds != null) {
            String json = mapper.writeValueAsString(userCreds);
            out.println(json);
            out.println(tokenHandler.getToken(json));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

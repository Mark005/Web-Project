package com.nncompany.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.nncompany.api.interfaces.ITokenHandler;
import com.nncompany.api.interfaces.IUserCredsService;
import com.nncompany.api.model.UserCreds;
import com.nncompany.di.Di;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logIn")
public class AutorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        IUserCredsService userCredsService = Di.getInstance().load(IUserCredsService.class);
        ITokenHandler tokenHandler = Di.getInstance().load(ITokenHandler.class);

        PrintWriter out = resp.getWriter();
        UserCreds requestUserCreds = mapper.readValue(req.getReader(), UserCreds.class);

        UserCreds userCreds = null;
        if(requestUserCreds.getLogin()!=null && requestUserCreds.getPass()!=null) {
            userCreds = userCredsService.getUserCredsByLogin(requestUserCreds.getLogin(), requestUserCreds.getPass());
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if(userCreds != null) {
            JsonObject userJsonObj = new JsonObject();
            userJsonObj.addProperty("id", userCreds.getUser().getId());
            String json = userJsonObj.toString();

            JsonObject token = new JsonObject();
            token.addProperty("token", tokenHandler.getToken(json));
            resp.setContentType("application/json");
            out.println(token.toString());
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

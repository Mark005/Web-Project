package com.nncompany.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nncompany.api.model.User;
import util.UserKeeper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/creds/user")
public class AuthentificationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        User user = UserKeeper.getLoggedUser();
        out.println(mapper.writeValueAsString(user));
    }
}

package Servlets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;

public class AutorizationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().setSubject(req.getHeader("user")).signWith(key).compact();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(req.getParameterMap());
        out.println(jws);

    }
}

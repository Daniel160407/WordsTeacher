package com.wordsteacher.wordsteacher.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logIn")
public class LogInServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();
    private String email;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/text");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(mySQLController.searchUser(email));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/text");

        String email = request.getParameter("email");

        if (mySQLController.getUser(email) != null) {
            this.email = email;
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }
}

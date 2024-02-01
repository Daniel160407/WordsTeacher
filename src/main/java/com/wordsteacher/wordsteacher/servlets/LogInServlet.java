package com.wordsteacher.wordsteacher.servlets;

import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/logIn")
public class LogInServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("works");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (mySQLController.getUser(email) != null) {
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }
}

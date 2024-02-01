package com.wordsteacher.wordsteacher.servlets;

import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("received");
        System.out.println(email);
        System.out.println(password);
        System.out.println();

        if (mySQLController.getUser(email) == null) {
            mySQLController.addUser(email, password);
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }
    }
}

package com.wordsteacher.wordsteacher.servlets;

import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/stateSaver")
public class UserStateSaverServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int userLevel = Integer.parseInt(request.getParameter("level"));

        mySQLController.setUserLevel(userId, userLevel);
    }
}

package com.wordsteacher.wordsteacher.servlets;

import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/wordsCounter")
public class WordsCounterServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/text");

        int userId = Integer.parseInt(request.getParameter("userId"));
        boolean getDroppedWordsAmount = Boolean.parseBoolean(request.getParameter("droppedWords"));
        System.out.println(getDroppedWordsAmount);

        PrintWriter writer = response.getWriter();

        try {
            if (getDroppedWordsAmount) {
                writer.println(mySQLController.getDroppedWordsAmount(userId));
                System.out.println("WordsAmount: " + mySQLController.getDroppedWordsAmount(userId));
            } else {
                writer.println(mySQLController.getWordsAmount(userId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

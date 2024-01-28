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

        PrintWriter writer = response.getWriter();

        try {
            writer.println(mySQLController.getWordsAmount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
package com.wordsteacher.wordsteacher.servlets;

import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/wordDropper")
public class WordDropperServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/text");

        int userId = Integer.parseInt(request.getParameter("userId"));

        BufferedReader reader = request.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        System.out.println("Json: " + jsonPayload);
        mySQLController.dropWords(userId, jsonPayload);
    }
}

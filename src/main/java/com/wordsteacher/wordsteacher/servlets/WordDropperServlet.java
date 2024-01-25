package com.wordsteacher.wordsteacher.servlets;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/wordDropper")
public class WordDropperServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonPayload = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonPayload.append(line);
        }

        mySQLController.dropWords(jsonPayload);
    }
}

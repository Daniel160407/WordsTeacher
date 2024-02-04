package com.wordsteacher.wordsteacher.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordsteacher.wordsteacher.JDBC.MySQLController;
import com.wordsteacher.wordsteacher.record.Word;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@WebServlet("/words")
public class WordsServlet extends HttpServlet {
    private final MySQLController mySQLController = new MySQLController();
    private int userId;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json");

        this.userId = Integer.parseInt(request.getParameter("userId"));

        try {
            List<Word> words = mySQLController.getWords(userId);
            PrintWriter printWriter = response.getWriter();

            ObjectMapper objectMapper = new ObjectMapper();
            printWriter.println(objectMapper.writeValueAsString(words));
            System.out.println(objectMapper.writeValueAsString(words));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setStatus(200);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/text");
        String word = request.getParameter("word");
        String meaning = request.getParameter("meaning");

        mySQLController.addWords(userId, word, meaning);

        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.println(mySQLController.getWordsAmount());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        mySQLController.deleteWords();
    }
}

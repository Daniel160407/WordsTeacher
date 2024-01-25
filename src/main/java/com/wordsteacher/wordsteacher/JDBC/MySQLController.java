package com.wordsteacher.wordsteacher.JDBC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordsteacher.wordsteacher.word.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLController implements JDBCController {
    private Connection con;

    @Override
    public void createSchema() {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            statement.execute("create schema if not exists words");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createTables() {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `words`.`words` (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `word` VARCHAR(45) NOT NULL,
                      `meaning` VARCHAR(45) NOT NULL,
                      PRIMARY KEY (`id`));
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `words`.`droppedwords` (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `word` VARCHAR(45) NOT NULL,
                      `meaning` VARCHAR(45) NOT NULL,
                      PRIMARY KEY (`id`));
                    """);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Word> getWords() throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        List<Word> words = new ArrayList<>();

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from words");

        while (resultSet.next()) {
            words.add(new Word(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
        }

        return words;
    }

    @Override
    public int getWordsAmount() throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select MAX(id) as id from words");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public void addWords(String word, String meaning) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into words (word,meaning) values (?,?)");
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, meaning);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void dropWords(StringBuilder jsonPayload) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            JsonNode jsonArray = new ObjectMapper().readTree(String.valueOf(jsonPayload));

            PreparedStatement preparedStatement = con.prepareStatement("insert into droppedWords (word,meaning) values (?,?)");
            for (JsonNode jsonNode : jsonArray) {
                String word = jsonNode.get("word").asText();
                String meaning = jsonNode.get("meaning").asText();

                preparedStatement.setString(1, word);
                preparedStatement.setString(2, meaning);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}

package com.wordsteacher.wordsteacher.JDBC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordsteacher.wordsteacher.record.User;
import com.wordsteacher.wordsteacher.record.Word;

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
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `words`.`users` (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `email` VARCHAR(45) NOT NULL,
                      `password` VARCHAR(45) NOT NULL,
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
        ResultSet resultSet = statement.executeQuery("select count(*) as count from words");

        int amount = 0;
        while (resultSet.next()) {
            amount = resultSet.getInt(1);
        }

        if (amount == 0) {
            returnWords();
        }

        resultSet = statement.executeQuery("select count(*) as count from droppedwords");
        while (resultSet.next()) {
            amount += resultSet.getInt(1);
        }

        System.out.println(amount);
        return amount;
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
            Statement statement = con.createStatement();
            JsonNode jsonArray = new ObjectMapper().readTree(String.valueOf(jsonPayload));

            PreparedStatement preparedStatement = con.prepareStatement("insert into droppedWords (word,meaning) values (?,?)");
            for (JsonNode jsonNode : jsonArray) {
                String word = jsonNode.get("word").asText();
                String meaning = jsonNode.get("meaning").asText();

                preparedStatement.setString(1, word);
                preparedStatement.setString(2, meaning);

                preparedStatement.executeUpdate();

                statement.executeUpdate("delete from words where word='" + word + "' and meaning='" + meaning + "'");
            }

            getWordsAmount();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void returnWords() {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from droppedWords");

            int amount = 0;
            while (resultSet.next()) {
                amount = resultSet.getInt(1);
            }

            if (amount == 100) {
                statement.executeUpdate("truncate table words");

                resultSet = statement.executeQuery("select word,meaning from droppedWords");

                PreparedStatement preparedStatement = con.prepareStatement("insert into words (word,meaning) values (?,?)");

                while (resultSet.next()) {
                    preparedStatement.setString(1, resultSet.getString(1));
                    preparedStatement.setString(2, resultSet.getString(2));
                    preparedStatement.executeUpdate();
                }

                statement.executeUpdate("truncate table droppedWords");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteWords() {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("delete from words");
            statement.executeUpdate("delete from droppedwords");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void addUser(String email, String password) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into users (email,password) values (?,?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(String email) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        User findenUser = null;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where email='" + email + "'");

            while (resultSet.next()) {
                findenUser = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return findenUser;
    }
}

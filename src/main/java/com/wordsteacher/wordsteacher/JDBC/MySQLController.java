package com.wordsteacher.wordsteacher.JDBC;

import com.wordsteacher.wordsteacher.word.Word;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    CREATE TABLE IF NOT EXISTS `words`.`nouns` (
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
    public List<Word> getNouns() throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words?useUnicode=true&characterEncoding=UTF-8", "root", "17042007");

        List<Word> words = new ArrayList<>();

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from nouns");

        while (resultSet.next()) {
            words.add(new Word(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
        }

        return words;
    }

    @Override
    public List<String> getVerb() {
        return null;
    }
}

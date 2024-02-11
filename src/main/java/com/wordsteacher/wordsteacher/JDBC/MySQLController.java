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
                                            `userid` INT NOT NULL,
                                            `wordid` INT NOT NULL);
                                          
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS `words`.`users` (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `email` VARCHAR(45) NOT NULL,
                      `password` VARCHAR(45) NOT NULL,
                      `level` INT NOT NULL,
                      PRIMARY KEY (`id`));
                    """);
            statement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS `words`.`userwords` (
                            `userid` INT NOT NULL,
                            `wordid` INT NOT NULL,
                            INDEX `userid` (`userid` ASC),
                            INDEX `wordid` (`wordid` ASC),
                            CONSTRAINT `wordid`
                                FOREIGN KEY (`wordid`)
                                REFERENCES `words`.`words` (`id`)
                                ON DELETE NO ACTION
                                ON UPDATE NO ACTION,
                            CONSTRAINT `userid`
                                FOREIGN KEY (`userid`)
                                REFERENCES `words`.`users` (`id`)
                                ON DELETE NO ACTION
                                ON UPDATE NO ACTION
                        );
                    """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Word> getWords(int userId) throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        List<Word> words = new ArrayList<>();

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select word, meaning from userwords join users on userid=users.id join words on wordid=words.id where users.id='"
                + userId + "'");

        int wordAmount = 0;
        while (resultSet.next()) {
            wordAmount++;
            words.add(new Word(wordAmount, resultSet.getString(1), resultSet.getString(2)));
        }

        return words;
    }

    @Override
    public int getWordsAmount(int userId) throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(*) as count from userWords where userid='" + userId + "'");

        int amount = 0;
        while (resultSet.next()) {
            amount = resultSet.getInt(1);
        }

        if (amount == 0) {
            returnWords(userId);
        }

        System.out.println(amount);
        return amount;
    }

    @Override
    public int getDroppedWordsAmount(int userId) throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(*) from droppedwords where userid='" + userId + "'");

        int amount = 0;
        while (resultSet.next()) {
            amount = resultSet.getInt(1);
        }

        return amount;
    }

    @Override
    public void addWords(int userId, String word, String meaning) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select word, meaning from words");

            boolean entrancePermission = true;
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(word) && resultSet.getString(2).equals(meaning)) {
                    entrancePermission = false;
                    break;
                }
            }

            if (entrancePermission) {
                PreparedStatement preparedStatement = con.prepareStatement("insert into words (word,meaning) values (?,?)");
                preparedStatement.setString(1, word);
                preparedStatement.setString(2, meaning);
                preparedStatement.executeUpdate();
            }

            resultSet = statement.executeQuery("select id from words where word='" + word + "' and meaning='" + meaning + "'");

            while (resultSet.next()) {
                PreparedStatement preparedStatement = con.prepareStatement("insert ignore into userWords values (?,?)");
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, resultSet.getInt(1));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropWords(int userId, StringBuilder jsonPayload) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();

            JsonNode jsonArray = new ObjectMapper().readTree(String.valueOf(jsonPayload));

            PreparedStatement preparedStatement = con.prepareStatement("insert into droppedWords (userid,wordid) values (?,?)");
            for (JsonNode jsonNode : jsonArray) {
                String word = jsonNode.get("word").asText();
                String meaning = jsonNode.get("meaning").asText();

                ResultSet resultSet = statement.executeQuery("select id from words where word='" + word + "' and meaning='" + meaning + "'");

                int wordId = 0;
                while (resultSet.next()) {
                    wordId = resultSet.getInt(1);
                }

                preparedStatement.setString(1, String.valueOf(userId));
                preparedStatement.setString(2, String.valueOf(wordId));

                preparedStatement.executeUpdate();

                statement.executeUpdate("delete from userwords where userid='" + userId + "' and wordid='" + wordId + "'");
            }

            getWordsAmount(userId);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void returnWords(int userId) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from droppedWords where userid='" + userId + "'");

            int amount = 0;
            while (resultSet.next()) {
                amount = resultSet.getInt(1);
            }

            if (amount == 2) {
                resultSet = statement.executeQuery("select wordid from droppedWords where userid='" + userId + "'");

                PreparedStatement preparedStatement = con.prepareStatement("insert into userwords values (?,?)");

                while (resultSet.next()) {
                    preparedStatement.setString(1, String.valueOf(userId));
                    preparedStatement.setString(2, resultSet.getString(1));
                    preparedStatement.executeUpdate();
                }

                statement.executeUpdate("delete from droppedwords where userid='" + userId + "'");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteWords(int userId) throws SQLException {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        Statement statement = con.createStatement();
        statement.executeUpdate("delete from userwords where userid='" + userId + "'");
    }

    @Override
    public void addUser(String email, String password) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into users (email,password,level) values (?,?,?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                findenUser = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return findenUser;
    }

    @Override
    public int searchUser(String email) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        int id = 0;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from users where email='" + email + "'");

            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    @Override
    public int getUserLevel(String email) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        int level = 0;
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select level from users where email='" + email + "'");

            while (resultSet.next()) {
                level = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return level;
    }

    @Override
    public void setUserLevel(int userId, int level) {
        con = MySQLConnector.getConnection("jdbc:mysql://localhost:3306/words", "root", "17042007");

        try {
            Statement statement = con.createStatement();
            System.out.println("level: " + level);
            statement.executeUpdate("update users set level='" + level + "' where id='" + userId + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

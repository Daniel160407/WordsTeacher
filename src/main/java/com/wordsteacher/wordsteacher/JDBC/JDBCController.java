package com.wordsteacher.wordsteacher.JDBC;

import com.wordsteacher.wordsteacher.record.User;
import com.wordsteacher.wordsteacher.record.Word;

import java.sql.SQLException;
import java.util.List;

public interface JDBCController {
    void createSchema();

    void createTables();

    List<Word> getWords(int userId) throws SQLException;

    int getWordsAmount(int userId) throws SQLException;

    int getDroppedWordsAmount(int userId) throws SQLException;

    void addWords(int userId, String word, String meaning);

    void dropWords(int userId, StringBuilder jsonPayload);

    void returnWords(int userId);

    void deleteWords(int userId) throws SQLException;

    void addUser(String email, String password);

    User getUser(String email);

    int searchUser(String email);

    int getUserLevel(String email);

    void setUserLevel(int userId, int level);
}

package com.wordsteacher.wordsteacher.JDBC;

import com.wordsteacher.wordsteacher.record.User;
import com.wordsteacher.wordsteacher.record.Word;

import java.sql.SQLException;
import java.util.List;

public interface JDBCController {
    void createSchema();

    void createTables();

    List<Word> getWords() throws SQLException;

    int getWordsAmount() throws SQLException;

    void addWords(String word, String meaning);

    void dropWords(StringBuilder jsonPayload);

    void returnWords();

    void deleteWords();

    void addUser(String email, String password);
    User getUser(String email);
}

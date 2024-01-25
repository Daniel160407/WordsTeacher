package com.wordsteacher.wordsteacher.JDBC;

import com.wordsteacher.wordsteacher.word.Word;

import java.sql.SQLException;
import java.util.List;

public interface JDBCController {
    void createSchema();

    void createTables();

    List<Word> getWords() throws SQLException;

    int getWordsAmount() throws SQLException;

    void addWords(String word, String meaning);

    void dropWords(StringBuilder jsonPayload);
}

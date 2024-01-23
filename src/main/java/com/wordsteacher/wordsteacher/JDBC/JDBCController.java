package com.wordsteacher.wordsteacher.JDBC;

import com.wordsteacher.wordsteacher.word.Word;

import java.sql.SQLException;
import java.util.List;

public interface JDBCController {
    void createSchema();

    void createTables();

    List<Word> getNouns() throws SQLException;

    List<String> getVerb();

}

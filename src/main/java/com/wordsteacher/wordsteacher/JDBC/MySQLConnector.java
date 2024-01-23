package com.wordsteacher.wordsteacher.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    public static Connection getConnection(String url, String username, String password) {
        Connection con;
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return con;
    }
}

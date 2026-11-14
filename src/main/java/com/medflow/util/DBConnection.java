package com.medflow.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** JDBC connection factory. Config via env vars, with local defaults. */
public final class DBConnection {
    private static final String URL  = env("DB_URL",  "jdbc:mysql://localhost:3306/medflow");
    private static final String USER = env("DB_USER", "root");
    private static final String PASS = env("DB_PASS", "root");

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }
}

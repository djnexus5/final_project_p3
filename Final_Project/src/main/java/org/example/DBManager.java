package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}

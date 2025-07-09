package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:postgresql://localhost:5432/ap-project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234"; // if you set one

    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to DB..."); // Debug line
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("DB Connection Failed!");
            e.printStackTrace(); // Critical for debugging
            throw e;
        }
    }
}


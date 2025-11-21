package com.studenttaskmanager.backend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsible for creating and managing the SQLite database connection.
 * This class keeps database logic separate from the rest of the backend.
 */
public class ConnectDB {

    // SQLite database file path (stored in project root after running)
    private static final String DB_URL = "jdbc:sqlite:TaskManager.db";

    private Connection connection;

    /**
     * Constructor opens database connection immediately when object is created.
     */
    public ConnectDB() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to SQLite.");
            e.printStackTrace();
        }
    }

    /**
     * Returns the active database connection for repository classes to use.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Safely closes the database connection.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("SQLite connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing SQLite connection.");
            e.printStackTrace();
        }
    }
}
package com.studenttaskmanager.backend.repository;

import com.studenttaskmanager.backend.db.ConnectDB;
import com.studenttaskmanager.backend.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all CRUD operations for students in the SQLite database.
 * This separates SQL logic from the UI and keeps the backend professional.
 */
public class StudentRepository {

    private final Connection connection;

    public StudentRepository(ConnectDB db) {
        this.connection = db.getConnection();
        createTableIfNeeded();
    }

    /**
     * Creates the students table if it does not exist.
     * Runs once automatically when StudentRepository is created.
     */
    private void createTableIfNeeded() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    class_name TEXT NOT NULL,
                    task TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Incomplete'
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'students' ready.");
        } catch (SQLException e) {
            System.out.println("Failed to create table.");
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new student record.
     */
    public boolean addStudent(Student s) {
        String sql = """
                INSERT INTO students (id, first_name, last_name, class_name, task, status)
                VALUES (?, ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getClassName());
            ps.setString(5, s.getTask());
            ps.setString(6, s.getStatus());

            ps.executeUpdate();
            System.out.println("Student inserted.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all students as a List.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("class_name"),
                        rs.getString("task"),
                        rs.getString("status")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Query failed.");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Update only the status for a student by ID.
     */
    public boolean updateStatus(int id, String newStatus) {
        String sql = "UPDATE students SET status = ? WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.out.println("No student found with id " + id);
                return false;
            }

            System.out.println("Status updated for id " + id);
            return true;

        } catch (SQLException e) {
            System.out.println("Status update failed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.out.println("No student found to delete.");
                return false;
            }

            System.out.println("Student deleted.");
            return true;

        } catch (SQLException e) {
            System.out.println("Delete failed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts demo records for testing.
     */
    public void seedSampleData() {
        // Check if there is already data in the table.
        List<Student> existing = getAllStudents();
        if (!existing.isEmpty()) {
            System.out.println("Seed skipped: students table already has data.");
            return;
        }

        // Only insert demo data when the table is empty.
        addStudent(new Student(10001, "John", "Doe", "Math 101", "Homework 1", "Incomplete"));
        addStudent(new Student(10002, "Alice", "Smith", "Science 202", "Lab Report", "Incomplete"));
        System.out.println("Sample data inserted.");
    }
}
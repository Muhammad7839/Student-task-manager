package com.studenttaskmanager.backend;

import com.studenttaskmanager.backend.models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic unit tests for the Student model.
 * These tests make sure the getters and setters work correctly.
 */
public class StudentTest {

    @Test
    public void testGettersAndSetters() {
        Student s = new Student();

        s.setId(12345);
        s.setFirstName("John");
        s.setLastName("Doe");
        s.setClassName("Math 101");
        s.setTask("Homework 1");
        s.setStatus("Incomplete");
        s.setCreatedAt("2024-01-01T10:00:00Z");
        s.setUpdatedAt("2024-01-02T11:00:00Z");

        assertEquals(12345, s.getId());
        assertEquals("John", s.getFirstName());
        assertEquals("Doe", s.getLastName());
        assertEquals("Math 101", s.getClassName());
        assertEquals("Homework 1", s.getTask());
        assertEquals("Incomplete", s.getStatus());
        assertEquals("2024-01-01T10:00:00Z", s.getCreatedAt());
        assertEquals("2024-01-02T11:00:00Z", s.getUpdatedAt());
    }
}
package com.studenttaskmanager.backend.models;

/**
 * Represents a student and their task in the system.
 * Each object of this class is stored as a document in the
 * "students" collection in Firebase Firestore.
 */
public class Student {

    // A simple numeric identifier for the student record.
    private int id;

    // Basic student info.
    private String firstName;
    private String lastName;
    private String className;

    // The task assigned to the student (homework, lab, project, etc.).
    private String task;

    // Current status of the task. Example values: "Incomplete", "Complete".
    private String status;

    // When this task record was created (ISO date-time, stored as String).
    private String createdAt;

    // Last time this task record was updated.
    private String updatedAt;

    /**
     * Required by Firebase/Firestore for automatic mapping.
     * Do not remove this empty constructor.
     */
    public Student() {
    }

    /**
     * Full constructor when we already know the id.
     * This can be useful for testing or manually creating records.
     */
    public Student(int id,
                   String firstName,
                   String lastName,
                   String className,
                   String task,
                   String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.task = task;
        this.status = status;
    }

    /**
     * Convenience constructor for new students where id may be assigned later.
     */
    public Student(String firstName,
                   String lastName,
                   String className,
                   String task,
                   String status) {
        this(0, firstName, lastName, className, task, status);
    }

    // --------------------
    // Getters and setters
    // --------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * When this record was first created.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * When this record was last updated.
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --------------------
    // Debug/Logging helper
    // --------------------

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                ", task='" + task + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
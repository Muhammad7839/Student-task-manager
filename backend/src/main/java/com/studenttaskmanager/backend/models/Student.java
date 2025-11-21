package com.studenttaskmanager.backend.models;

/**
 * Model class that represents one student task record in the system.
 * Firebase requires a no-argument constructor for automatic mapping.
 */
public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String className;
    private String task;
    private String status; // "Incomplete" or "Complete"

    /**
     * Required by Firebase/Firestore for automatic document-to-object mapping.
     * Do NOT remove this constructor.
     */
    public Student() {
    }

    /**
     * Full constructor when we already know the id.
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
                '}';
    }
}
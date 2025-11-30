package frontend.model;

import java.time.LocalDate;

/**
 * Simple task model used by the JavaFX UI.
 * id is the Firebase/backend id for this task.
 */
public class Task {

    private int id;              // backend id (from Firebase)
    private String title;
    private String course;
    private LocalDate dueDate;
    private String priority;
    private String status;
    private String notes;

    // Full constructor with id – used when loading from Firebase
    public Task(int id,
                String title,
                String course,
                LocalDate dueDate,
                String priority,
                String status,
                String notes) {

        this.id = id;
        this.title = title;
        this.course = course;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.notes = notes;
    }

    // Old constructor without id – keeps existing UI code working
    public Task(String title,
                String course,
                LocalDate dueDate,
                String priority,
                String status,
                String notes) {

        this(0, title, course, dueDate, priority, status, notes);
    }

    // --- getters ---

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    // --- setters ---

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
package frontend.model;

import java.time.LocalDate;

public class Task {

    private String title;
    private String course;
    private LocalDate dueDate;
    private String priority;
    private String status;
    private String notes;

    public Task(String title, String course, LocalDate dueDate,
                String priority, String status, String notes) {

        this.title = title;
        this.course = course;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.notes = notes;
    }

    public String getTitle() { return title; }
    public String getCourse() { return course; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

public void setTitle(String title) { this.title = title; }
public void setCourse(String course) { this.course = course; }
public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
public void setPriority(String priority) { this.priority = priority; }
public void setStatus(String status) { this.status = status; }
public void setNotes(String notes) { this.notes = notes; }
}
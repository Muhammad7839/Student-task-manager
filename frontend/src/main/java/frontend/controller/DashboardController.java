package frontend.controller;

import frontend.MainApp;
import frontend.Service.TaskService;
import frontend.model.Task;
import frontend.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private ProgressBar overallProgress;
    @FXML private TextField newTaskTitle;   // quick task text box on the dashboard

    @FXML
    private void initialize() {
        if (overallProgress != null && overallProgress.getProgress() < 0) {
            overallProgress.setProgress(0.0);
        }

        checkAndShowTaskAlerts();
    }

    // -----------------------------
    // SMART QUICK-TASK CREATION
    // -----------------------------

    // Base helper used by all quick-task buttons
    private void createQuickTaskWithCategory(String category) {
        if (newTaskTitle == null) {
            System.out.println("newTaskTitle TextField not wired from FXML");
            return;
        }

        String rawTitle = newTaskTitle.getText();
        String title = rawTitle == null ? "" : rawTitle.trim();

        if (title.isEmpty()) {
            NotificationUtil.showError("Please enter a task title first.");
            return;
        }

        String course;
        LocalDate dueDate;
        String priority;
        String status = "Not started";
        String notes = "";

        // 1) Choose defaults based on which mini-button was clicked
        switch (category) {
            case "MEETING":
                course = "Meetings / Events";
                dueDate = LocalDate.now();              // today
                priority = "High";
                notes = "Quick meeting added from dashboard.";
                break;

            case "DESIGN":
                course = "Capstone / Design Work";
                dueDate = LocalDate.now().plusDays(3);
                priority = "Medium";
                notes = "Design task created via quick entry.";
                break;

            case "PLANNER":
                course = "Study Plan / Weekly Planner";
                dueDate = LocalDate.now().plusDays(7);
                priority = "Medium";
                notes = "Planning task to organize your week.";
                break;

            case "SURVEY":
                course = "Feedback / Survey";
                dueDate = LocalDate.now().plusDays(1);
                priority = "Low";
                notes = "Don’t forget to answer this survey.";
                break;

            default: // plain “Create Task” button
                course = "General";
                dueDate = LocalDate.now();
                priority = "Medium";
                notes = "";
                break;
        }

        // 2) Tiny “AI-ish” tweak: bump priority based on keywords
        String lower = title.toLowerCase();
        if (lower.contains("exam") || lower.contains("midterm") || lower.contains("final")) {
            priority = "High";
        } else if (lower.contains("homework") || lower.contains("assignment")) {
            if (!"High".equals(priority)) {
                priority = "Medium";
            }
        }

        // 3) Create and save the task (goes to Firebase through TaskService)
        Task quickTask = new Task(
                title,
                course,
                dueDate,
                priority,
                status,
                notes
        );

        TaskService.addTask(quickTask);
        newTaskTitle.clear();

        // 4) Friendly summary for the user
        String message =
                "Quick task created:\n" +
                        "- Course: " + course + "\n" +
                        "- Due: " + dueDate + "\n" +
                        "- Priority: " + priority + "\n\n" +
                        "You can edit details anytime in the Tasks view.";

        NotificationUtil.showSuccess(message);
    }

    // Default “Create Task” button on the dashboard
    @FXML
    private void createQuickTask() {
        createQuickTaskWithCategory("DEFAULT");
    }

    // Optional: hook these to the small Meeting / Design / Planner / Survey buttons in Dashboard.fxml
    @FXML
    private void handleQuickMeeting() {
        createQuickTaskWithCategory("MEETING");
    }

    @FXML
    private void handleQuickDesign() {
        createQuickTaskWithCategory("DESIGN");
    }

    @FXML
    private void handleQuickPlanner() {
        createQuickTaskWithCategory("PLANNER");
    }

    @FXML
    private void handleQuickSurvey() {
        createQuickTaskWithCategory("SURVEY");
    }

    // -----------------------------
    // REMINDER POPUP
    // -----------------------------

    private void checkAndShowTaskAlerts() {
        LocalDate today = LocalDate.now();

        List<Task> dueToday = new ArrayList<>();
        List<Task> overdue = new ArrayList<>();

        for (Task task : TaskService.getTasks()) {
            if (task == null || task.getDueDate() == null) {
                continue;
            }

            LocalDate due = task.getDueDate();

            if (due.isBefore(today)) {
                overdue.add(task);
            } else if (due.isEqual(today)) {
                dueToday.add(task);
            }
        }

        if (dueToday.isEmpty() && overdue.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder();

        if (!dueToday.isEmpty()) {
            message.append("Tasks due today: ").append(dueToday.size()).append("\n");
            for (Task t : dueToday) {
                message.append(" • ").append(t.getTitle()).append("\n");
            }
            message.append("\n");
        }

        if (!overdue.isEmpty()) {
            message.append("Overdue tasks: ").append(overdue.size()).append("\n");
            for (Task t : overdue) {
                message.append(" • ").append(t.getTitle())
                        .append(" (was due ")
                        .append(t.getDueDate())
                        .append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task reminders");
        alert.setHeaderText("You have important tasks to check");
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
}
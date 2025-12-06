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

/**
 * Controller for the main dashboard screen.
 * <p>
 * This controller handles:
 * - The overall progress bar at the top of the dashboard,
 * - The quick task creation input and buttons,
 * - A reminder popup for tasks that are due today or overdue.
 */
public class DashboardController {

    @FXML private ProgressBar overallProgress;
    @FXML private TextField newTaskTitle;   // quick task text box on the dashboard

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * Ensures the progress bar starts from a valid state
     * and shows a reminder alert about tasks that are due or overdue.
     */
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

    /**
     * Creates a quick task based on the given category.
     * <p>
     * This is the shared helper for all quick-task buttons.
     * It reads the title from the {@code newTaskTitle} text field,
     * applies different default values depending on the category,
     * slightly adjusts priority based on keywords in the title,
     * saves the task via {@link TaskService#addTask(Task)},
     * and shows a success notification.
     *
     * @param category type of quick task, for example "MEETING", "DESIGN",
     *                 "PLANNER", "SURVEY", or "DEFAULT"
     */
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

        // 2) Small keyword-based tweak for priority
        String lower = title.toLowerCase();
        if (lower.contains("exam") || lower.contains("midterm") || lower.contains("final")) {
            priority = "High";
        } else if (lower.contains("homework") || lower.contains("assignment")) {
            if (!"High".equals(priority)) {
                priority = "Medium";
            }
        }

        // 3) Create and save the task (TaskService handles persistence)
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

    /**
     * Handler for the default "Create Task" button on the dashboard.
     * <p>
     * Uses the {@code DEFAULT} category.
     */
    @FXML
    private void createQuickTask() {
        createQuickTaskWithCategory("DEFAULT");
    }

    /**
     * Handler for the quick "Meeting" button on the dashboard.
     * <p>
     * Creates a task with meeting-related defaults.
     */
    @FXML
    private void handleQuickMeeting() {
        createQuickTaskWithCategory("MEETING");
    }

    /**
     * Handler for the quick "Design" button on the dashboard.
     * <p>
     * Creates a task with design / capstone-related defaults.
     */
    @FXML
    private void handleQuickDesign() {
        createQuickTaskWithCategory("DESIGN");
    }

    /**
     * Handler for the quick "Planner" button on the dashboard.
     * <p>
     * Creates a task focused on planning or weekly organization.
     */
    @FXML
    private void handleQuickPlanner() {
        createQuickTaskWithCategory("PLANNER");
    }

    /**
     * Handler for the quick "Survey" button on the dashboard.
     * <p>
     * Creates a lower-priority reminder to fill out a survey or feedback form.
     */
    @FXML
    private void handleQuickSurvey() {
        createQuickTaskWithCategory("SURVEY");
    }

    // -----------------------------
    // REMINDER POPUP
    // -----------------------------

    /**
     * Checks all tasks and shows a reminder popup for tasks that are
     * due today or are already overdue.
     * <p>
     * This method builds a human-readable summary and displays it
     * in a simple information {@link Alert}.
     * If there are no such tasks, it does nothing.
     */
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

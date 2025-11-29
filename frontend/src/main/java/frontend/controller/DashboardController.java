package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import frontend.Service.TaskService;
import frontend.model.Task;
import frontend.util.NotificationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private ProgressBar overallProgress;
    @FXML private TextField newTaskTitle;

    @FXML
    private void initialize() {
        if (overallProgress != null && overallProgress.getProgress() < 0) {
            overallProgress.setProgress(0.0);
        }

        checkAndShowTaskAlerts();
    }

    @FXML
    private void createQuickTask() {
        String raw = newTaskTitle.getText();
        String title = raw == null ? "" : raw.trim();

        if (title.isEmpty()) {
            NotificationUtil.showError("Please enter a task title");
            return;
        }

        Task quickTask = new Task(
                title,
                "Quick task",
                LocalDate.now(),
                "Medium",
                "Not started",
                ""
        );

        TaskService.addTask(quickTask);
        newTaskTitle.clear();

        NotificationUtil.showSuccess("Quick task created");
    }

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

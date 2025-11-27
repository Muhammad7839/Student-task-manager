package frontend.controller;

import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class AnalyticsController {

    @FXML private Label totalTasksLabel;
    @FXML private Label completedTasksLabel;
    @FXML private Label inProgressTasksLabel;
    @FXML private Label notStartedTasksLabel;
    @FXML private Label overdueTasksLabel;
    @FXML private Label todayTasksLabel;
    @FXML private Label upcomingWeekTasksLabel;

    @FXML
    private void initialize() {
        ObservableList<Task> tasks = TaskService.getTasks();

        // run once on load
        updateStats(tasks);

        // update whenever tasks list changes (add/remove/replace)
        tasks.addListener((ListChangeListener<Task>) change -> {
            updateStats(tasks);
        });
    }

    private void updateStats(ObservableList<Task> tasks) {
        int total = 0;
        int completed = 0;
        int inProgress = 0;
        int notStarted = 0;
        int overdue = 0;
        int todayCount = 0;
        int upcomingWeek = 0;

        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);

        for (Task task : tasks) {
            if (task == null) continue;

            total++;

            String status = task.getStatus() != null ? task.getStatus() : "";
            if (status.equalsIgnoreCase("Completed")) {
                completed++;
            } else if (status.equalsIgnoreCase("In progress")) {
                inProgress++;
            } else if (status.equalsIgnoreCase("Not started")) {
                notStarted++;
            }

            if (task.getDueDate() != null) {
                LocalDate due = task.getDueDate();
                if (due.isBefore(today)) {
                    overdue++;
                } else if (due.isEqual(today)) {
                    todayCount++;
                } else if (!due.isAfter(weekFromNow)) {
                    upcomingWeek++;
                }
            }
        }

        totalTasksLabel.setText(String.valueOf(total));
        completedTasksLabel.setText(String.valueOf(completed));
        inProgressTasksLabel.setText(String.valueOf(inProgress));
        notStartedTasksLabel.setText(String.valueOf(notStarted));
        overdueTasksLabel.setText(String.valueOf(overdue));
        todayTasksLabel.setText(String.valueOf(todayCount));
        upcomingWeekTasksLabel.setText(String.valueOf(upcomingWeek));
    }
}

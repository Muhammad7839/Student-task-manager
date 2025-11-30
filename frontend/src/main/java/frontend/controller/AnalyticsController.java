package frontend.controller;

import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
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

    @FXML private Label todaySummaryLabel;

    @FXML private ComboBox<String> filterChoice;

    @FXML private BarChart<String, Number> statusChart;

    private ObservableList<Task> tasks;

    @FXML
    private void initialize() {
        tasks = TaskService.getTasks();

        if (filterChoice != null) {
            filterChoice.getItems().addAll(
                    "All tasks",
                    "Completed",
                    "In progress",
                    "Not started",
                    "Overdue",
                    "Today",
                    "Next 7 days"
            );
            filterChoice.setValue("All tasks");

            filterChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateStats();
            });
        }

        updateStats();

        tasks.addListener((ListChangeListener<Task>) change -> {
            updateStats();
        });
    }

    private void updateStats() {
        if (tasks == null) return;

        int total = 0;
        int completed = 0;
        int inProgress = 0;
        int notStarted = 0;
        int overdue = 0;
        int todayCount = 0;
        int upcomingWeek = 0;

        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);

        String filter = filterChoice != null && filterChoice.getValue() != null
                ? filterChoice.getValue()
                : "All tasks";

        for (Task task : tasks) {
            if (task == null) continue;

            if (!matchesFilter(task, filter, today, weekFromNow)) {
                continue;
            }

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

        if (todaySummaryLabel != null) {
            String summary = "Today " + todayCount + " due, "
                    + overdue + " overdue, "
                    + upcomingWeek + " due in the next 7 days";
            todaySummaryLabel.setText(summary);
        }

        if (statusChart != null) {
            statusChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Completed", completed));
            series.getData().add(new XYChart.Data<>("In progress", inProgress));
            series.getData().add(new XYChart.Data<>("Not started", notStarted));
            series.getData().add(new XYChart.Data<>("Overdue", overdue));

            statusChart.getData().add(series);
        }
    }

    private boolean matchesFilter(Task task,
                                  String filter,
                                  LocalDate today,
                                  LocalDate weekFromNow) {

        if (filter == null || filter.equals("All tasks")) {
            return true;
        }

        String status = task.getStatus() != null ? task.getStatus() : "";
        LocalDate due = task.getDueDate();

        switch (filter) {
            case "Completed":
                return status.equalsIgnoreCase("Completed");
            case "In progress":
                return status.equalsIgnoreCase("In progress");
            case "Not started":
                return status.equalsIgnoreCase("Not started");
            case "Overdue":
                return due != null && due.isBefore(today);
            case "Today":
                return due != null && due.isEqual(today);
            case "Next 7 days":
                return due != null
                        && due.isAfter(today)
                        && !due.isAfter(weekFromNow);
            default:
                return true;
        }
    }
}

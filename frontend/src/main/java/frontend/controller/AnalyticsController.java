package frontend.controller;

import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the analytics screen.
 * <p>
 * This controller reads the shared task list from {@link TaskService},
 * calculates summary statistics, updates the labels and bar chart,
 * and allows the user to export a CSV report for the current month
 * (or all tasks if the current month has no tasks).
 */
public class AnalyticsController {

    @FXML private Label totalTasksLabel;
    @FXML private Label completedTasksLabel;
    @FXML private Label inProgressTasksLabel;
    @FXML private Label notStartedTasksLabel;
    @FXML private Label overdueTasksLabel;
    @FXML private Label todayTasksLabel;
    @FXML private Label upcomingWeekTasksLabel;

    @FXML private Label todaySummaryLabel;

    /**
     * Filter choice for analytics view.
     * Supported values:
     * All tasks, Completed, In progress, Not started, Overdue, Today, Next 7 days.
     */
    @FXML private ComboBox<String> filterChoice;

    /**
     * Bar chart showing the count of tasks by status.
     */
    @FXML private BarChart<String, Number> statusChart;

    // Button is defined in FXML with onAction="#handleExportMonth"
    @FXML private Button exportMonthBtn;

    /**
     * Observable list of all tasks shared by the application.
     * This is obtained from {@link TaskService#getTasks()}.
     */
    private ObservableList<Task> tasks;

    /**
     * Initializes the controller after the FXML is loaded.
     * <p>
     * This method
     * 1. Loads the tasks from TaskService,
     * 2. Sets up the filter combo box,
     * 3. Computes initial statistics,
     * 4. Adds a listener so that analytics update automatically when tasks change.
     */
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

            // Whenever the user changes the filter, recalculate the statistics
            filterChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateStats();
            });
        }

        // Calculate initial statistics
        updateStats();

        // Recalculate stats whenever the underlying task list changes
        tasks.addListener((ListChangeListener<Task>) change -> {
            updateStats();
        });
    }

    /**
     * Handler for the export button, called from FXML.
     * Delegates to {@link #handleExportMonthReport()}.
     */
    @FXML
    private void handleExportMonth() {
        handleExportMonthReport();
    }

    /**
     * Recomputes all analytics numbers based on the current tasks and current filter,
     * and updates the labels and bar chart in the UI.
     */
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

            // Skip tasks that do not match the current filter
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
            // Clear and rebuild the bar chart with the new numbers
            statusChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Completed", completed));
            series.getData().add(new XYChart.Data<>("In progress", inProgress));
            series.getData().add(new XYChart.Data<>("Not started", notStarted));
            series.getData().add(new XYChart.Data<>("Overdue", overdue));

            statusChart.getData().add(series);
        }
    }

    /**
     * Checks whether a given task should be included for analytics
     * under the currently selected filter.
     *
     * @param task        the task to check
     * @param filter      current filter label from the combo box
     * @param today       today's date
     * @param weekFromNow date 7 days from today
     * @return true if this task matches the filter, false otherwise
     */
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

    // -------------------------
    // Export current month report (with smart fallback)
    // -------------------------

    /**
     * Exports a CSV report for the current month.
     * <p>
     * Behavior:
     * If there are tasks with a due date in the current month,
     * only those tasks are exported.
     * If there are no tasks for the current month,
     * the method falls back to exporting all tasks instead
     * and informs the user in the alert message.
     * <p>
     * The user can choose the file path using a {@link FileChooser}.
     */
    private void handleExportMonthReport() {
        if (tasks == null || tasks.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION,
                    "No data",
                    "There are no tasks to export yet.");
            return;
        }

        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        List<Task> monthTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (t == null) continue;
            LocalDate due = t.getDueDate();
            if (due != null &&
                    due.getYear() == currentYear &&
                    due.getMonthValue() == currentMonth) {
                monthTasks.add(t);
            }
        }

        boolean exportingAll = false;

        // If no tasks match this month, fall back to all tasks
        if (monthTasks.isEmpty()) {
            monthTasks = new ArrayList<>(tasks);
            exportingAll = true;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save monthly report");

        DateTimeFormatter fileFmt = DateTimeFormatter.ofPattern("yyyy-MM");
        String baseName = "tasks-" + now.format(fileFmt) + ".csv";
        chooser.setInitialFileName(baseName);

        File file = chooser.showSaveDialog(
                totalTasksLabel != null
                        ? totalTasksLabel.getScene().getWindow()
                        : null
        );

        // User cancelled the dialog
        if (file == null) {
            return;
        }

        try (PrintWriter out = new PrintWriter(file)) {
            // Header row
            out.println("Title,Course,DueDate,Priority,Status,Notes");

            DateTimeFormatter dateFmt = DateTimeFormatter.ISO_LOCAL_DATE;

            for (Task t : monthTasks) {
                String title = safeCsv(t.getTitle());
                String course = safeCsv(t.getCourse());
                String due = t.getDueDate() != null
                        ? t.getDueDate().format(dateFmt)
                        : "";
                String priority = safeCsv(t.getPriority());
                String status = safeCsv(t.getStatus());
                String notes = safeCsv(t.getNotes());

                out.printf("%s,%s,%s,%s,%s,%s%n",
                        title, course, due, priority, status, notes);
            }

            if (exportingAll) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Report saved",
                        "No tasks had a due date in this month.\n" +
                                "A report for ALL tasks was saved:\n" +
                                file.getAbsolutePath());
            } else {
                showAlert(Alert.AlertType.INFORMATION,
                        "Report saved",
                        "Monthly report saved:\n" + file.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error saving report",
                    "Could not save the report file.");
        }
    }

    /**
     * Escapes a string so it is safe to place inside a CSV cell.
     * <p>
     * If the value contains a comma, quotes, or line breaks,
     * it is wrapped in double quotes and internal quotes are doubled.
     *
     * @param value original string value, may be null
     * @return CSV-safe string, never null
     */
    private String safeCsv(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v + "\"";
        }
        return v;
    }

    /**
     * Convenience method to show a simple JavaFX alert dialog.
     *
     * @param type  type of alert (information, error, etc.)
     * @param title title of the alert window
     * @param msg   message body of the alert
     */
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

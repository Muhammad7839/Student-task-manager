package frontend.controller;

import frontend.MainApp;
import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CalendarController {

    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Button prevMonthBtn;
    @FXML
    private Button nextMonthBtn;

    private YearMonth currentMonth;

    @FXML
    private void initialize() {
        // start at current month
        currentMonth = YearMonth.now();
        refreshCalendar();
    }

    @FXML
    private void handlePrevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        refreshCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        refreshCalendar();
    }

    private void refreshCalendar() {
        if (monthLabel != null) {
            String monthName = currentMonth.getMonth().name().toLowerCase();
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
            monthLabel.setText(monthName + " " + currentMonth.getYear());
        }

        if (calendarGrid == null) return;

        calendarGrid.getChildren().clear();

        LocalDate firstOfMonth = currentMonth.atDay(1);
        DayOfWeek firstDow = firstOfMonth.getDayOfWeek();

        // Java's DayOfWeek is MONDAY=1..SUNDAY=7, we want Monday-based index 0..6
        int firstColumn = firstDow.getValue() - 1;

        int daysInMonth = currentMonth.lengthOfMonth();

        int row = 0;
        int col = firstColumn;

        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            VBox cell = createDayCell(date, date.equals(today));

            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDayCell(LocalDate date, boolean isToday) {
        int dayOfMonth = date.getDayOfMonth();

        Label dayLabel = new Label(String.valueOf(dayOfMonth));
        dayLabel.getStyleClass().add("task-title");

        VBox box = new VBox(dayLabel);
        box.setSpacing(4);
        box.getStyleClass().add("card");
        box.setMinHeight(70);

        // highlight today's date
        if (isToday) {
            box.setStyle("-fx-border-color: #00B0E1; -fx-border-radius: 10;");
            dayLabel.setTooltip(new Tooltip("Today"));
        }

        // tasks for this date
        List<Task> tasksForDate = getTasksForDate(date);
        int taskCount = tasksForDate.size();

        if (taskCount > 0) {
            Label tasksLabel = new Label(taskCount + " task" + (taskCount > 1 ? "s" : ""));
            tasksLabel.getStyleClass().add("muted");
            box.getChildren().add(tasksLabel);

            // tooltip with task titles
            StringBuilder tooltipText = new StringBuilder();
            int limit = Math.min(taskCount, 3);
            for (int i = 0; i < limit; i++) {
                tooltipText.append("- ").append(tasksForDate.get(i).getTitle()).append("\n");
            }
            if (taskCount > 3) {
                tooltipText.append("…");
            }

            Tooltip tooltip = new Tooltip(tooltipText.toString().trim());
            Tooltip.install(box, tooltip);
        }

        // click handler for date
        box.setOnMouseClicked(e -> handleDateClicked(date));

        return box;
    }

    private List<Task> getTasksForDate(LocalDate date) {
        List<Task> result = new ArrayList<>();
        for (Task task : TaskService.getTasks()) {
            if (task.getDueDate() != null && task.getDueDate().isEqual(date)) {
                result.add(task);
            }
        }
        return result;
    }

    private void handleDateClicked(LocalDate date) {
        List<Task> tasksForDate = getTasksForDate(date);

        if (tasksForDate.isEmpty()) {
            System.out.println("No tasks on " + date);
            return;
        }

        if (tasksForDate.size() == 1) {
            // go straight to details for the only task
            Task single = tasksForDate.get(0);
            TaskService.setSelectedTask(single);
            MainApp.showTaskDetails();
        } else {
            // show a simple popup with all tasks for that date
            StringBuilder message = new StringBuilder("Tasks on " + date + ":\n\n");
            for (Task t : tasksForDate) {
                message.append("• ").append(t.getTitle());
                if (t.getCourse() != null && !t.getCourse().isBlank()) {
                    message.append(" (").append(t.getCourse()).append(")");
                }
                message.append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tasks on " + date);
            alert.setHeaderText(null);
            alert.setContentText(message.toString().trim());
            alert.showAndWait();
        }
    }
}

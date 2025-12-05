package frontend.controller;

import frontend.Service.TaskService;
import frontend.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarController {

    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;

    private YearMonth currentMonth;

    @FXML
    private void initialize() {
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
            String monthName = currentMonth.getMonth().name();
            String prettyMonth = monthName.substring(0, 1)
                    + monthName.substring(1).toLowerCase();
            monthLabel.setText(prettyMonth + " " + currentMonth.getYear());
        }

        if (calendarGrid == null) return;

        calendarGrid.getChildren().clear();

        LocalDate firstOfMonth = currentMonth.atDay(1);
        DayOfWeek firstDow = firstOfMonth.getDayOfWeek();
        int firstColumn = firstDow.getValue() - 1; // Monday = 1

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
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.getStyleClass().add("task-title");

        VBox box = new VBox(dayLabel);
        box.setSpacing(4);
        // important: add dedicated class so dark CSS can target it
        box.getStyleClass().addAll("card", "calendar-cell");
        box.setMinHeight(70);

        if (isToday) {
            box.getStyleClass().add("calendar-today");
            dayLabel.setTooltip(new Tooltip("Today"));
        }

        List<Task> tasksForDay = TaskService.getTasks().stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isEqual(date))
                .collect(Collectors.toList());

        if (!tasksForDay.isEmpty()) {
            Label countLabel = new Label(
                    tasksForDay.size() + " task" + (tasksForDay.size() > 1 ? "s" : "")
            );
            countLabel.getStyleClass().add("muted");
            box.getChildren().add(countLabel);

            String tooltipText = tasksForDay.stream()
                    .map(Task::getTitle)
                    .collect(Collectors.joining("\n"));
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(box, tooltip);
        }

        box.setOnMouseClicked(e -> {
            if (tasksForDay.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No tasks");
                alert.setHeaderText(null);
                alert.setContentText("No tasks due on " + date);
                alert.showAndWait();
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Tasks due on ").append(date).append(":\n\n");
                for (Task t : tasksForDay) {
                    msg.append("• ").append(t.getTitle());
                    if (t.getCourse() != null && !t.getCourse().isBlank()) {
                        msg.append(" (").append(t.getCourse()).append(")");
                    }
                    msg.append("\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tasks for " + date);
                alert.setHeaderText(
                        "You have " + tasksForDay.size()
                                + (tasksForDay.size() == 1 ? " task" : " tasks")
                                + " due on this day"
                );
                alert.setContentText(msg.toString());
                alert.showAndWait();
            }
        });

        return box;
    }
}
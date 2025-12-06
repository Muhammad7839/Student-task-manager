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

/**
 * Controller for the calendar view.
 * <p>
 * This controller shows a month view calendar, highlights today,
 * and displays how many tasks are due on each day.
 * Clicking on a day shows a dialog listing all tasks due on that date.
 */
public class CalendarController {

    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;

    /**
     * The month currently shown in the calendar.
     */
    private YearMonth currentMonth;

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * Sets the current month to the current date and renders the calendar.
     */
    @FXML
    private void initialize() {
        currentMonth = YearMonth.now();
        refreshCalendar();
    }

    /**
     * Handles clicking the "previous month" control in the UI.
     * <p>
     * Moves the view one month back and refreshes the calendar.
     */
    @FXML
    private void handlePrevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        refreshCalendar();
    }

    /**
     * Handles clicking the "next month" control in the UI.
     * <p>
     * Moves the view one month forward and refreshes the calendar.
     */
    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        refreshCalendar();
    }

    /**
     * Rebuilds the calendar grid for the current month.
     * <p>
     * This method
     * 1. Updates the month label,
     * 2. Clears any old cells,
     * 3. Creates a {@link VBox} cell for each day and places it
     *    in the correct row and column based on its day of week.
     */
    private void refreshCalendar() {
        if (monthLabel != null) {
            String monthName = currentMonth.getMonth().name();
            String prettyMonth = monthName.substring(0, 1)
                    + monthName.substring(1).toLowerCase();
            monthLabel.setText(prettyMonth + " " + currentMonth.getYear());
        }

        if (calendarGrid == null) return;

        // Remove any existing day cells before drawing the new month
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

    /**
     * Creates a single calendar cell for a given date.
     * <p>
     * The cell shows the day number, optionally a count of tasks due,
     * and sets up tooltips and click behavior.
     *
     * @param date    the date this cell represents
     * @param isToday true if this date is today's date
     * @return a VBox node ready to be added to the calendar grid
     */
    private VBox createDayCell(LocalDate date, boolean isToday) {
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        // Reuse an existing text style for consistency with the rest of the app
        dayLabel.getStyleClass().add("task-title");

        VBox box = new VBox(dayLabel);
        box.setSpacing(4);
        // Use these classes so the global CSS (including dark theme) can style calendar cells
        box.getStyleClass().addAll("card", "calendar-cell");
        box.setMinHeight(70);

        if (isToday) {
            box.getStyleClass().add("calendar-today");
            dayLabel.setTooltip(new Tooltip("Today"));
        }

        // Find all tasks with a due date equal to this specific day
        List<Task> tasksForDay = TaskService.getTasks().stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isEqual(date))
                .collect(Collectors.toList());

        if (!tasksForDay.isEmpty()) {
            Label countLabel = new Label(
                    tasksForDay.size() + " task" + (tasksForDay.size() > 1 ? "s" : "")
            );
            countLabel.getStyleClass().add("muted");
            box.getChildren().add(countLabel);

            // Tooltip shows a simple list of task titles for this date
            String tooltipText = tasksForDay.stream()
                    .map(Task::getTitle)
                    .collect(Collectors.joining("\n"));
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(box, tooltip);
        }

        // On click, show a dialog with more details about the tasks for this date
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

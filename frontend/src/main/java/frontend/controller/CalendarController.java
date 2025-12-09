package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

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
            monthLabel.setText(currentMonth.getMonth().name().substring(0, 1)
                    + currentMonth.getMonth().name().substring(1).toLowerCase()
                    + " " + currentMonth.getYear());
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

            VBox cell = createDayCell(day, date.equals(today));

            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDayCell(int dayOfMonth, boolean isToday) {
        Label dayLabel = new Label(String.valueOf(dayOfMonth));
        dayLabel.getStyleClass().add("task-title");

        VBox box = new VBox(dayLabel);
        box.setSpacing(4);
        box.getStyleClass().add("card");
        box.setMinHeight(70);

        // highlight today's date
        if (isToday) {
            box.setStyle("-fx-border-color: #00B0E1; -fx-border-radius: 10;");
            dayLabel.setTooltip(new Tooltip("Today"));   // FIXED: Tooltip goes on label
        }

        // click handler for date
        box.setOnMouseClicked(e ->
                System.out.println("Clicked date: "
                        + currentMonth.getYear() + "-"
                        + currentMonth.getMonthValue() + "-"
                        + dayOfMonth));

        return box;
    }
}

package frontend.controller;

import frontend.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class TaskDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label courseLabel;
    @FXML private Label dueLabel;
    @FXML private Label priorityLabel;
    @FXML private Label statusLabel;
    @FXML private Label notesLabel;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setTask(Task task) {
        if (task == null) return;

        if (titleLabel != null)   titleLabel.setText(nullSafe(task.getTitle()));
        if (courseLabel != null)  courseLabel.setText(nullSafe(task.getCourse()));
        if (priorityLabel != null) priorityLabel.setText(nullSafe(task.getPriority()));
        if (statusLabel != null)  statusLabel.setText(nullSafe(task.getStatus()));
        if (notesLabel != null)   notesLabel.setText(nullSafe(task.getNotes()));

        if (dueLabel != null) {
            if (task.getDueDate() != null) {
                dueLabel.setText(task.getDueDate().format(DATE_FMT));
            } else {
                dueLabel.setText("No due date");
            }
        }
    }

    private String nullSafe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    @FXML
    private void handleClose() {
        if (titleLabel != null && titleLabel.getScene() != null) {
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.close();
        }
    }
}
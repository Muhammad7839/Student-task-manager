package frontend.controller;

import frontend.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Controller for the task details popup window.
 * <p>
 * This screen displays all information about a selected task in a
 * simple read-only view. The parent controller calls {@link #setTask(Task)}
 * to populate the labels before showing the window.
 */
public class TaskDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label courseLabel;
    @FXML private Label dueLabel;
    @FXML private Label priorityLabel;
    @FXML private Label statusLabel;
    @FXML private Label notesLabel;

    /**
     * Common date format used for displaying task due dates.
     */
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Populates the detail labels with the data from the given task.
     * <p>
     * If a field is missing or blank (such as no notes or no due date),
     * a readable fallback is shown instead.
     *
     * @param task the task whose details should be shown
     */
    public void setTask(Task task) {
        if (task == null) return;

        if (titleLabel != null)    titleLabel.setText(nullSafe(task.getTitle()));
        if (courseLabel != null)   courseLabel.setText(nullSafe(task.getCourse()));
        if (priorityLabel != null) priorityLabel.setText(nullSafe(task.getPriority()));
        if (statusLabel != null)   statusLabel.setText(nullSafe(task.getStatus()));
        if (notesLabel != null)    notesLabel.setText(nullSafe(task.getNotes()));

        if (dueLabel != null) {
            if (task.getDueDate() != null) {
                dueLabel.setText(task.getDueDate().format(DATE_FMT));
            } else {
                dueLabel.setText("No due date");
            }
        }
    }

    /**
     * Returns a fallback string for null or empty values.
     *
     * @param s input string
     * @return a non-blank value safe for labels
     */
    private String nullSafe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    /**
     * Closes the task details popup window.
     * <p>
     * Triggered by a Close button in the FXML.
     */
    @FXML
    private void handleClose() {
        if (titleLabel != null && titleLabel.getScene() != null) {
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.close();
        }
    }
}

package frontend.controller;

import frontend.MainApp;
import frontend.model.Task;
import frontend.Service.TaskService;
import frontend.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the Add / Edit Task screen.
 *
 * <p>This screen is used in two modes:
 * <ul>
 *   <li>Create a brand new task</li>
 *   <li>Edit an existing task selected from the Tasks table</li>
 * </ul>
 * The mode is decided based on {@link TaskService#getEditingTask()}.</p>
 */
public class AddTaskController {

    @FXML private TextField titleField;
    @FXML private TextField courseField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> priorityField;
    @FXML private ComboBox<String> statusField;
    @FXML private TextArea notesField;

    /**
     * True when the user is editing an existing task
     * instead of creating a new one.
     */
    private boolean editMode = false;

    /**
     * The task currently being edited, if any.
     */
    private Task editingTask;

    /**
     * Initializes the form, populates combo boxes,
     * and pre-fills fields when editing an existing task.
     */
    @FXML
    private void initialize() {
        if (priorityField != null) {
            priorityField.getItems().addAll("Low", "Medium", "High");
        }
        if (statusField != null) {
            statusField.getItems().addAll("Not started", "In progress", "Completed", "Overdue");
        }

        // Check if we are editing an existing task
        editingTask = TaskService.getEditingTask();
        if (editingTask != null) {
            editMode = true;

            titleField.setText(editingTask.getTitle());
            courseField.setText(editingTask.getCourse());
            dueDatePicker.setValue(editingTask.getDueDate());
            priorityField.setValue(editingTask.getPriority());
            statusField.setValue(editingTask.getStatus());
            notesField.setText(editingTask.getNotes());
        }
    }

    /**
     * Cancels the operation and returns to the Tasks screen
     * without saving changes.
     */
    @FXML
    private void handleCancel() {
        TaskService.clearEditingTask();
        MainApp.showTasks();
    }

    /**
     * Validates the form and either creates a new task
     * or updates the existing one.
     *
     * <p>When editing, changes are persisted through
     * {@link TaskService#saveTask(Task)}. When creating,
     * {@link TaskService#addTask(Task)} is used.</p>
     */
    @FXML
    private void handleSave() {
        // simple validation
        String title = titleField.getText();
        String course = courseField.getText();

        if (title == null || title.isBlank() ||
                course == null || course.isBlank()) {
            NotificationUtil.showError("Please enter a title and course.");
            return;
        }

        if (editMode && editingTask != null) {
            // Update existing task
            editingTask.setTitle(title);
            editingTask.setCourse(course);
            editingTask.setDueDate(dueDatePicker.getValue());
            editingTask.setPriority(priorityField.getValue());
            editingTask.setStatus(statusField.getValue());
            editingTask.setNotes(notesField.getText());

            // Persist changes (Firebase / DB)
            TaskService.saveTask(editingTask);

        } else {
            // Create new task
            Task newTask = new Task(
                    title,
                    course,
                    dueDatePicker.getValue(),
                    priorityField.getValue(),
                    statusField.getValue(),
                    notesField.getText()
            );

            TaskService.addTask(newTask);
        }

        NotificationUtil.showSuccess("Task saved");

        TaskService.clearEditingTask();
        MainApp.showTasks();
    }
}

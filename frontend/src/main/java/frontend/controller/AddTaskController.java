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

public class AddTaskController {

    @FXML private TextField titleField;
    @FXML private TextField courseField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> priorityField;
    @FXML private ComboBox<String> statusField;
    @FXML private TextArea notesField;

    private boolean editMode = false;
    private Task editingTask;

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

    @FXML
    private void handleCancel() {
        TaskService.clearEditingTask();
        MainApp.showTasks();
    }

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
            // UPDATE EXISTING TASK
            editingTask.setTitle(title);
            editingTask.setCourse(course);
            editingTask.setDueDate(dueDatePicker.getValue());
            editingTask.setPriority(priorityField.getValue());
            editingTask.setStatus(statusField.getValue());
            editingTask.setNotes(notesField.getText());

            // IMPORTANT: actually persist the changes to Firebase
            TaskService.saveTask(editingTask);

        } else {
            // CREATE NEW TASK
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
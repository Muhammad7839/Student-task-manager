package frontend.controller;

import frontend.MainApp;
import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

public class TaskDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label courseLabel;
    @FXML private Label dueDateLabel;
    @FXML private Label priorityLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea notesArea;

    private Task task;

    @FXML
    private void initialize() {
        task = TaskService.getSelectedTask();

        if (task == null) {
            System.out.println("No task selected for details, going back to Tasks");
            MainApp.showTasks();
            return;
        }

        titleLabel.setText(task.getTitle());
        courseLabel.setText(task.getCourse());
        dueDateLabel.setText(task.getDueDate() != null ? task.getDueDate().toString() : "");
        priorityLabel.setText(task.getPriority());
        statusLabel.setText(task.getStatus());
        notesArea.setText(task.getNotes() != null ? task.getNotes() : "");
    }

    @FXML
    private void handleBackToTasks() {
        TaskService.clearSelectedTask();
        MainApp.showTasks();
    }

    @FXML
    private void handleEditFromDetails() {
        if (task == null) {
            return;
        }
        TaskService.setEditingTask(task);
        MainApp.showAddTask();
    }

    @FXML
    private void handleDeleteFromDetails() {
        if (task == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this task?");

        var result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            TaskService.removeTask(task);
            TaskService.clearSelectedTask();
            MainApp.showTasks();
        }
    }
}

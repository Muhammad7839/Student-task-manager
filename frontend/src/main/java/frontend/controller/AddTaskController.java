package frontend.controller;

import frontend.MainApp;
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

    @FXML
    private void initialize() {
        if (priorityField != null) {
            priorityField.getItems().addAll("Low", "Medium", "High");
        }
        if (statusField != null) {
            statusField.getItems().addAll("Not started", "In progress", "Completed", "Overdue");
        }
    }

    @FXML
    private void handleCancel() {
        MainApp.showTasks();
    }

    @FXML
    private void handleSave() {
        // later, create a TaskRow from these values and add it to a shared list
        System.out.println("Saving task:");
        System.out.println("Title = " + titleField.getText());
        System.out.println("Course = " + courseField.getText());
        System.out.println("Due date = " + (dueDatePicker.getValue() != null ? dueDatePicker.getValue() : ""));
        System.out.println("Priority = " + priorityField.getValue());
        System.out.println("Status = " + statusField.getValue());
        System.out.println("Notes = " + notesField.getText());

        MainApp.showTasks();
    }
}

package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class DashboardController {

    @FXML private ProgressBar overallProgress;
    @FXML private TextField newTaskTitle;

    @FXML
    private void initialize() {
        if (overallProgress != null && overallProgress.getProgress() < 0) {
            overallProgress.setProgress(0.0);
        }
    }

    @FXML
    private void createQuickTask() {
        String title = newTaskTitle.getText();
        System.out.println("Create task: " + (title == null ? "" : title.trim()));
        newTaskTitle.clear();
        // later: append to task list, persist to backend, show toast
    }
}

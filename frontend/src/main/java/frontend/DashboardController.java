package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class DashboardController {

    @FXML private ProgressBar overallProgress;
    @FXML private TextField newTaskTitle;

    @FXML
    private void initialize() {
        if (overallProgress != null) overallProgress.setProgress(0.5);
    }

    @FXML
    private void createQuickTask() {
        // stub. later: add to list and reset field
        newTaskTitle.clear();
    }
}

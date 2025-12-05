package frontend.controller;

import frontend.MainApp;
import frontend.Service.TaskService;
import frontend.model.Task;
import frontend.util.NotificationUtil;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class TasksController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> priorityFilter;

    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> courseColumn;
    @FXML private TableColumn<Task, LocalDate> dueColumn;
    @FXML private TableColumn<Task, String> priorityColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    private FilteredList<Task> filteredTasks;

    @FXML
    private void initialize() {
        // table columns
        if (titleColumn != null)
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (courseColumn != null)
            courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        if (dueColumn != null)
            dueColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        if (priorityColumn != null)
            priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        if (statusColumn != null)
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // observable list from TaskService
        filteredTasks = new FilteredList<>(TaskService.getTasks(), t -> true);
        tasksTable.setItems(filteredTasks);

        // filters
        if (statusFilter != null) {
            statusFilter.setItems(FXCollections.observableArrayList(
                    "All", "Not started", "In progress", "Completed", "Overdue"
            ));
            statusFilter.setValue("All");
        }

        if (priorityFilter != null) {
            priorityFilter.setItems(FXCollections.observableArrayList(
                    "All", "Low", "Medium", "High"
            ));
            priorityFilter.setValue("All");
        }

        if (searchField != null) {
            searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        }

        applyFilters();
    }

    @FXML
    private void applyFilters() {
        String searchText = searchField != null && searchField.getText() != null
                ? searchField.getText().toLowerCase().trim()
                : "";

        String status = (statusFilter != null && statusFilter.getValue() != null)
                ? statusFilter.getValue()
                : "All";

        String priority = (priorityFilter != null && priorityFilter.getValue() != null)
                ? priorityFilter.getValue()
                : "All";

        filteredTasks.setPredicate(task -> {
            if (task == null) return false;

            // search on title / course
            if (!searchText.isEmpty()) {
                String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";
                String course = task.getCourse() != null ? task.getCourse().toLowerCase() : "";
                if (!title.contains(searchText) && !course.contains(searchText)) {
                    return false;
                }
            }

            // status filter
            if (!"All".equals(status)) {
                String s = task.getStatus() != null ? task.getStatus() : "";
                if (!s.equalsIgnoreCase(status)) {
                    return false;
                }
            }

            // priority filter
            if (!"All".equals(priority)) {
                String p = task.getPriority() != null ? task.getPriority() : "";
                if (!p.equalsIgnoreCase(priority)) {
                    return false;
                }
            }

            return true;
        });
    }

    // ---------------- buttons ----------------

    @FXML
    private void handleNewTask() {
        // clear any editing state
        TaskService.setEditingTask(null);
        openAddTaskScreen();
    }

    @FXML
    private void handleEditTask() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationUtil.showError("Please select a task to edit.");
            return;
        }

        TaskService.setEditingTask(selected);
        openAddTaskScreen();
    }

    @FXML
    private void handleDeleteTask() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationUtil.showError("Please select a task to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete task");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this task?");
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                TaskService.removeTask(selected);
                NotificationUtil.showSuccess("Task deleted");
            }
        });
    }

    @FXML
    private void handleViewDetails() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationUtil.showError("Please select a task to view.");
            return;
        }

        openTaskDetailsDialog(selected);
    }

    // ---------------- navigation helpers (no MainApp.showAddTask / showTaskDetails) ----------------

    private void openAddTaskScreen() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/frontend/fxml/AddTask.fxml")
            );
            // replace the main scene root
            MainApp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            NotificationUtil.showError("Could not open Add Task screen.");
        }
    }

    private void openTaskDetailsDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/frontend/fxml/TaskDetails.fxml")
            );
            Parent root = loader.load();

            // pass task into controller
            TaskDetailsController controller = loader.getController();
            controller.setTask(task);

            Stage dialog = new Stage();
            dialog.setTitle("Task details");
            dialog.initOwner(MainApp.getPrimaryStage());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            NotificationUtil.showError("Could not open task details.");
        }
    }
}
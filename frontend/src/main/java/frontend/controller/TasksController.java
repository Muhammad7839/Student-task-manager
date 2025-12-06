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

/**
 * Controller for the main Tasks screen.
 * <p>
 * This screen shows all tasks in a table and lets the user:
 *  - Search by title or course,
 *  - Filter by status and priority,
 *  - Create new tasks,
 *  - Edit or delete existing tasks,
 *  - Open a read-only details dialog for a task.
 */
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

    /**
     * Wrapper around the global task list that lets us apply filters.
     */
    private FilteredList<Task> filteredTasks;

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * This method:
     *  - Binds table columns to Task properties,
     *  - Wraps the shared task list in a {@link FilteredList},
     *  - Populates the status and priority filter combo boxes,
     *  - Wires the search field to re-apply filters on text change,
     *  - Applies the initial filter (show all tasks).
     */
    @FXML
    private void initialize() {
        // Set up table column bindings
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

        // Observable list from TaskService, wrapped so we can filter it
        filteredTasks = new FilteredList<>(TaskService.getTasks(), t -> true);
        tasksTable.setItems(filteredTasks);

        // Status filter options
        if (statusFilter != null) {
            statusFilter.setItems(FXCollections.observableArrayList(
                    "All", "Not started", "In progress", "Completed", "Overdue"
            ));
            statusFilter.setValue("All");
        }

        // Priority filter options
        if (priorityFilter != null) {
            priorityFilter.setItems(FXCollections.observableArrayList(
                    "All", "Low", "Medium", "High"
            ));
            priorityFilter.setValue("All");
        }

        // Re-apply filters whenever the search text changes
        if (searchField != null) {
            searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        }

        applyFilters();
    }

    /**
     * Applies search, status, and priority filters to the table.
     * <p>
     * This method reads the current values of the search field and the
     * two combo boxes, then updates the predicate of {@link #filteredTasks}.
     */
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

            // Search by title or course
            if (!searchText.isEmpty()) {
                String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";
                String course = task.getCourse() != null ? task.getCourse().toLowerCase() : "";
                if (!title.contains(searchText) && !course.contains(searchText)) {
                    return false;
                }
            }

            // Status filter
            if (!"All".equals(status)) {
                String s = task.getStatus() != null ? task.getStatus() : "";
                if (!s.equalsIgnoreCase(status)) {
                    return false;
                }
            }

            // Priority filter
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

    /**
     * Handler for the "New Task" button.
     * <p>
     * Clears any editing state and opens the Add Task screen
     * in create mode.
     */
    @FXML
    private void handleNewTask() {
        // Clear any editing state
        TaskService.setEditingTask(null);
        openAddTaskScreen();
    }

    /**
     * Handler for the "Edit Task" button.
     * <p>
     * Uses the currently selected row in the table.
     * If nothing is selected, shows an error notification.
     */
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

    /**
     * Handler for the "Delete Task" button.
     * <p>
     * Asks for confirmation and removes the selected task
     * from {@link TaskService} if the user confirms.
     */
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

    /**
     * Handler for the "View details" button.
     * <p>
     * Opens a modal dialog showing all properties for the selected task.
     */
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

    /**
     * Opens the Add Task screen.
     * <p>
     * This replaces the current root of the main stage's scene with
     * the root loaded from {@code AddTask.fxml}. Whether it acts as
     * "add" or "edit" depends on {@link TaskService#getEditingTask()}.
     */
    private void openAddTaskScreen() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/frontend/fxml/AddTask.fxml")
            );
            // Replace the main scene root
            MainApp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            NotificationUtil.showError("Could not open Add Task screen.");
        }
    }

    /**
     * Opens a modal dialog that shows the details of a single task.
     *
     * @param task the task whose details will be displayed
     */
    private void openTaskDetailsDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/frontend/fxml/TaskDetails.fxml")
            );
            Parent root = loader.load();

            // Pass task into controller
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

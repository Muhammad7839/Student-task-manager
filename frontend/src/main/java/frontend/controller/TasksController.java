package frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import frontend.MainApp;
import frontend.model.Task;
import frontend.Service.TaskService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import frontend.util.NotificationUtil;

public class TasksController {

    @FXML private TableView<TaskRow> tasksTable;
    @FXML private TableColumn<TaskRow, String> titleColumn;
    @FXML private TableColumn<TaskRow, String> courseColumn;
    @FXML private TableColumn<TaskRow, String> dueColumn;
    @FXML private TableColumn<TaskRow, String> priorityColumn;
    @FXML private TableColumn<TaskRow, String> statusColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> priorityFilter;

    private final ObservableList<TaskRow> allTasks = FXCollections.observableArrayList();
    private FilteredList<TaskRow> filteredTasks;

    @FXML
    private void initialize() {
        if (titleColumn != null) {
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        }
        if (courseColumn != null) {
            courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        }
        if (dueColumn != null) {
            dueColumn.setCellValueFactory(new PropertyValueFactory<>("due"));
        }
        if (priorityColumn != null) {
            priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }

        loadTasksFromService();

        if (statusFilter != null) {
            statusFilter.getItems().addAll(
                    "All statuses",
                    "Not started",
                    "In progress",
                    "Completed",
                    "Overdue"
            );
            statusFilter.getSelectionModel().selectFirst();
        }

        if (priorityFilter != null) {
            priorityFilter.getItems().addAll(
                    "All priorities",
                    "Low",
                    "Medium",
                    "High"
            );
            priorityFilter.getSelectionModel().selectFirst();
        }

        if (tasksTable != null) {
            filteredTasks = new FilteredList<>(allTasks, task -> true);

            SortedList<TaskRow> sorted = new SortedList<>(filteredTasks);
            sorted.comparatorProperty().bind(tasksTable.comparatorProperty());

            tasksTable.setItems(sorted);
        }

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        }
    }

    private void loadTasksFromService() {
        allTasks.clear();

        for (Task task : TaskService.getTasks()) {
            allTasks.add(new TaskRow(task));
        }
    }

    @FXML
    private void handleNewTask() {
        MainApp.showAddTask();
    }

    @FXML
    private void handleEditTask() {
        TaskRow selected = tasksTable != null
                ? tasksTable.getSelectionModel().getSelectedItem()
                : null;

        if (selected == null) {
            System.out.println("No task selected to edit");
            return;
        }

        TaskService.setEditingTask(selected.getTask());
        MainApp.showAddTask();
    }

    @FXML
    private void handleViewDetails() {
        TaskRow selected = tasksTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("No task selected to view details");
            return;
        }

        TaskService.setSelectedTask(selected.getTask());
        MainApp.showTaskDetails();
    }

    @FXML
    private void handleDeleteTask() {
        TaskRow selected = tasksTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("No task selected to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this task?");

        var result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            TaskService.removeTask(selected.getTask());
            allTasks.remove(selected);

            NotificationUtil.showSuccess("Task deleted");
        }
    }

    @FXML
    private void applyFilters() {
        updateFilter();
    }

    private void updateFilter() {
        if (filteredTasks == null) {
            return;
        }

        String searchText = searchField != null ? searchField.getText() : "";
        String status = statusFilter != null ? statusFilter.getValue() : "All statuses";
        String priority = priorityFilter != null ? priorityFilter.getValue() : "All priorities";

        String searchLower = searchText == null ? "" : searchText.toLowerCase().trim();

        filteredTasks.setPredicate(task -> {
            if (task == null) {
                return false;
            }

            if (status != null && !status.equals("All statuses")) {
                if (!status.equalsIgnoreCase(task.getStatus())) {
                    return false;
                }
            }

            if (priority != null && !priority.equals("All priorities")) {
                if (!priority.equalsIgnoreCase(task.getPriority())) {
                    return false;
                }
            }

            if (searchLower != null && !searchLower.isEmpty()) {
                boolean matchesTitle = task.getTitle().toLowerCase().contains(searchLower);
                boolean matchesCourse = task.getCourse().toLowerCase().contains(searchLower);
                return matchesTitle || matchesCourse;
            }

            return true;
        });
    }

    public static class TaskRow {
        private final Task task;
        private final String title;
        private final String course;
        private final String due;
        private final String priority;
        private final String status;

        public TaskRow(Task task) {
            this.task = task;
            this.title = task.getTitle();
            this.course = task.getCourse();
            this.due = task.getDueDate() != null ? task.getDueDate().toString() : "";
            this.priority = task.getPriority();
            this.status = task.getStatus();
        }

        public Task getTask() { return task; }
        public String getTitle() { return title; }
        public String getCourse() { return course; }
        public String getDue() { return due; }
        public String getPriority() { return priority; }
        public String getStatus() { return status; }
    }
}

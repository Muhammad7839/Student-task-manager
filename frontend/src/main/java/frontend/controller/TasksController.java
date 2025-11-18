package frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import frontend.MainApp;

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
        // table column wiring
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

        // sample data, later this will come from your model or backend
        allTasks.addAll(
                new TaskRow("Finish CSC 251 homework", "CSC 251 Discrete Math", "Fri", "High", "In progress"),
                new TaskRow("Read chapter 3", "CSC 343 Operating Systems", "Mon", "Medium", "Not started"),
                new TaskRow("Group meeting slides", "Capstone Student Task Manager", "Today", "High", "Completed"),
                new TaskRow("Quiz review sheet", "CSC 371 Mobile Dev", "Tomorrow", "Low", "In progress")
        );

        // set up filters
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

        // connect filtered list to table
        if (tasksTable != null) {
            filteredTasks = new FilteredList<>(allTasks, task -> true);

            SortedList<TaskRow> sorted = new SortedList<>(filteredTasks);
            sorted.comparatorProperty().bind(tasksTable.comparatorProperty());

            tasksTable.setItems(sorted);
        }

        // search live listener
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        }
    }

    @FXML
    private void handleNewTask() {
        // later this will open your Add Task screen
        MainApp.showAddTask();
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

            // status filter
            if (status != null && !status.equals("All statuses")) {
                if (!status.equalsIgnoreCase(task.getStatus())) {
                    return false;
                }
            }

            // priority filter
            if (priority != null && !priority.equals("All priorities")) {
                if (!priority.equalsIgnoreCase(task.getPriority())) {
                    return false;
                }
            }

            // search filter: match in title or course
            if (searchLower != null && !searchLower.isEmpty()) {
                boolean matchesTitle = task.getTitle().toLowerCase().contains(searchLower);
                boolean matchesCourse = task.getCourse().toLowerCase().contains(searchLower);
                return matchesTitle || matchesCourse;
            }

            return true;
        });
    }

    public static class TaskRow {
        private final String title;
        private final String course;
        private final String due;
        private final String priority;
        private final String status;

        public TaskRow(String title, String course, String due, String priority, String status) {
            this.title = title;
            this.course = course;
            this.due = due;
            this.priority = priority;
            this.status = status;
        }

        public String getTitle() { return title; }
        public String getCourse() { return course; }
        public String getDue() { return due; }
        public String getPriority() { return priority; }
        public String getStatus() { return status; }
    }
}

package frontend.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import frontend.model.Task;

public class TaskService {

    private static final ObservableList<Task> tasks =
            FXCollections.observableArrayList();

    // task being edited
    private static Task editingTask;

    // task whose details we want to show
    private static Task selectedTask;

    public static ObservableList<Task> getTasks() {
        return tasks;
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void removeTask(Task task) {
        tasks.remove(task);
    }

    // edit mode
    public static void setEditingTask(Task task) {
        editingTask = task;
    }

    public static Task getEditingTask() {
        return editingTask;
    }

    public static void clearEditingTask() {
        editingTask = null;
    }

    // details mode
    public static void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public static Task getSelectedTask() {
        return selectedTask;
    }

    public static void clearSelectedTask() {
        selectedTask = null;
    }
}

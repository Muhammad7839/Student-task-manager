package frontend.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import frontend.model.Task;

import java.time.LocalDate;

public class TaskService {

    private static final ObservableList<Task> tasks =
            FXCollections.observableArrayList();

    private static Task editingTask;
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

    // edit mode support
    public static void setEditingTask(Task task) {
        editingTask = task;
    }

    public static Task getEditingTask() {
        return editingTask;
    }

    public static void clearEditingTask() {
        editingTask = null;
    }

    // details screen support
    public static void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public static Task getSelectedTask() {
        return selectedTask;
    }

    public static void clearSelectedTask() {
        selectedTask = null;
    }

    // “database operations test” – preload sample data once
    public static void initSampleDataIfEmpty() {
        if (!tasks.isEmpty()) {
            return;
        }

        tasks.add(new Task(
                "Finish CSC 251 homework",
                "CSC 251 Discrete Math",
                LocalDate.now().plusDays(1),
                "High",
                "In progress",
                "Work on Chapter 9 problems"
        ));

        tasks.add(new Task(
                "Read OS chapter 3",
                "CSC 343 Operating Systems",
                LocalDate.now().plusDays(3),
                "Medium",
                "Not started",
                "Focus on process scheduling"
        ));

        tasks.add(new Task(
                "Capstone slide deck",
                "Student Task Manager Capstone",
                LocalDate.now().minusDays(1),
                "High",
                "Not started",
                "Prepare demo slides and screenshots"
        ));

        tasks.add(new Task(
                "Android lab review",
                "CSC 371 Mobile Dev",
                LocalDate.now(),
                "Low",
                "Completed",
                "Review Week 6 lab before quiz"
        ));

        System.out.println("TaskService sample data loaded, count = " + tasks.size());
    }
}

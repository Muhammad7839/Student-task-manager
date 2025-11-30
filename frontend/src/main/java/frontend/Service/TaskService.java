package frontend.Service;

import frontend.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// backend imports
import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;
import com.studenttaskmanager.backend.repository.FirebaseStudentRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * TaskService now works as a bridge between the JavaFX UI and Firebase.
 * UI still talks to TaskService. TaskService talks to Firebase.
 */
public class TaskService {

    // list used by the UI (table views, etc.)
    private static final ObservableList<Task> tasks =
            FXCollections.observableArrayList();

    // used by Add / Edit / Details screens (same as before)
    private static Task editingTask;
    private static Task selectedTask;

    // backend repository (Firebase)
    private static final FirebaseStudentRepository repo;

    // simple counter to generate new ids for tasks
    private static int nextId = 20000;

    static {
        // make sure Firebase is ready
        FirebaseConfig.init();
        repo = new FirebaseStudentRepository();
    }

    // ---------------------
    // Public API for UI
    // ---------------------

    public static ObservableList<Task> getTasks() {
        return tasks;
    }

    /**
     * Load all tasks from Firebase into the local ObservableList.
     * Call this once when the app starts (e.g. in MainApp or first screen).
     */
    public static void loadFromBackend() {
        tasks.clear();

        List<Student> students = repo.getAllStudents();
        updateNextId(students);

        for (Student s : students) {
            tasks.add(fromStudent(s));
        }

        System.out.println("Loaded " + tasks.size() + " tasks from Firebase");
    }

    /**
     * This keeps the old name so existing code still compiles.
     * Now it just calls loadFromBackend instead of inserting fake data.
     */
    public static void initSampleDataIfEmpty() {
        loadFromBackend();
    }

    /**
     * Add a new task. Also saves it to Firebase.
     */
    public static void addTask(Task task) {
        if (task.getId() == 0) {
            task.setId(nextId++);
        }

        Student s = toStudent(task);
        boolean ok = repo.addStudent(s);

        if (ok) {
            tasks.add(task);
            System.out.println("Task added and saved to Firebase, id=" + task.getId());
        } else {
            System.out.println("Failed to save task to Firebase");
        }
    }

    /**
     * Remove a task from memory and Firebase.
     */
    public static void removeTask(Task task) {
        if (task == null) return;

        boolean ok = repo.deleteStudent(task.getId());
        if (ok) {
            tasks.remove(task);
            System.out.println("Task deleted from Firebase, id=" + task.getId());
        } else {
            System.out.println("Failed to delete task from Firebase");
        }
    }

    /**
     * Save edits for an existing task (overwrites the record in Firebase).
     * You can call this after user edits title, status, etc.
     */
    public static void saveTask(Task task) {
        if (task == null) return;
        if (task.getId() == 0) {
            // if somehow still no id, treat as new
            addTask(task);
            return;
        }

        Student s = toStudent(task);
        boolean ok = repo.addStudent(s); // set() in repo will overwrite existing

        if (ok) {
            System.out.println("Task updated in Firebase, id=" + task.getId());
        } else {
            System.out.println("Failed to update task in Firebase");
        }
    }

    // edit mode support (same as before)

    public static void setEditingTask(Task task) {
        editingTask = task;
    }

    public static Task getEditingTask() {
        return editingTask;
    }

    public static void clearEditingTask() {
        editingTask = null;
    }

    // details screen support (same as before)

    public static void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public static Task getSelectedTask() {
        return selectedTask;
    }

    public static void clearSelectedTask() {
        selectedTask = null;
    }

    // ---------------------
    // Helper methods
    // ---------------------

    // map backend Student -> frontend Task
    private static Task fromStudent(Student s) {
        if (s == null) return null;

        int id = s.getId();
        String title = s.getTask();        // Task title
        String course = s.getClassName();  // Course name

        // We did not store these in Firebase in option A,
        // so we just give sensible defaults for display.
        LocalDate dueDate = null;
        String priority = "Medium";
        String status = s.getStatus();
        String notes = "";

        return new Task(id, title, course, dueDate, priority, status, notes);
    }

    // map frontend Task -> backend Student
    private static Student toStudent(Task t) {
        if (t == null) return null;

        int id = t.getId();

        // firstName and lastName are not used in UI, so we keep them simple
        String firstName = "Student";
        String lastName = "Task";

        String className = t.getCourse();
        String taskText = t.getTitle();
        String status = t.getStatus();

        return new Student(id, firstName, lastName, className, taskText, status);
    }

    // figure out the next id to use based on what is in Firebase
    private static void updateNextId(List<Student> students) {
        int max = nextId;
        for (Student s : students) {
            if (s != null && s.getId() > max) {
                max = s.getId();
            }
        }
        nextId = max + 1;
    }
}
package frontend.Service;

import frontend.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Backend imports – use our Firebase-based repository
import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;
import com.studenttaskmanager.backend.repository.FirebaseStudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskService is the bridge between the JavaFX UI and the backend.
 * The UI only talks to TaskService. TaskService talks to Firebase.
 */
public class TaskService {

    // List used by the UI (tables, lists, etc.)
    private static final ObservableList<Task> tasks =
            FXCollections.observableArrayList();

    // Used by Add / Edit / Details screens
    private static Task editingTask;
    private static Task selectedTask;

    // Backend repository (Firebase)
    private static final FirebaseStudentRepository repo;

    // Simple counter to generate new IDs for tasks
    private static int nextId = 20000;

    // Static initializer runs once when the class is loaded
    static {
        // Make sure Firebase is ready before any calls
        FirebaseConfig.init();
        repo = new FirebaseStudentRepository();
    }

    // ---------------------
    // Public API for UI
    // ---------------------

    // Expose the observable list to controllers
    public static ObservableList<Task> getTasks() {
        return tasks;
    }

    /**
     * Load all tasks from Firebase into the local ObservableList.
     * Call this once when the app starts (for example in the first screen).
     */
    public static void loadFromBackend() {
        tasks.clear();

        List<Student> students = repo.getAllStudents();
        updateNextId(students);

        for (Student s : students) {
            Task t = fromStudent(s);
            if (t != null) {
                tasks.add(t);
            }
        }

        System.out.println("Loaded " + tasks.size() + " tasks from Firebase");
    }

    /**
     * Old name kept for compatibility with existing controller code.
     * Internally, it just calls loadFromBackend().
     */
    public static void loadTasksFromBackend() {
        loadFromBackend();
    }

    /**
     * Keeps the old name so existing code still compiles.
     * Instead of loading fake data, we now load real data from Firebase.
     */
    public static void initSampleDataIfEmpty() {
        loadFromBackend();
    }

    /**
     * Add a new task and save it to Firebase.
     */
    public static void addTask(Task task) {
        if (task == null) return;

        // If this is a new task, give it an ID
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
     * Remove a task from memory and from Firebase.
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
     * Call this after the user edits a task’s fields.
     */
    public static void saveTask(Task task) {
        if (task == null) return;

        // If somehow the task still has no ID, treat it as a new one
        if (task.getId() == 0) {
            addTask(task);
            return;
        }

        Student s = toStudent(task);
        boolean ok = repo.addStudent(s); // set() in repo overwrites existing doc

        if (ok) {
            System.out.println("Task updated in Firebase, id=" + task.getId());
        } else {
            System.out.println("Failed to update task in Firebase");
        }
    }

    // ---------------------
    // Edit mode support
    // ---------------------

    public static void setEditingTask(Task task) {
        editingTask = task;
    }

    public static Task getEditingTask() {
        return editingTask;
    }

    public static void clearEditingTask() {
        editingTask = null;
    }

    // ---------------------
    // Details screen support
    // ---------------------

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

    // Map backend Student -> frontend Task
    private static Task fromStudent(Student s) {
        if (s == null) return null;

        int id = s.getId();
        String title = s.getTask();        // Task title
        String course = s.getClassName();  // Course name

        // We are not storing dueDate/priority/notes in Firebase yet,
        // so we use reasonable defaults for the UI.
        LocalDate dueDate = null;
        String priority = "Medium";
        String status = s.getStatus();
        String notes = "";

        return new Task(id, title, course, dueDate, priority, status, notes);
    }

    // Map frontend Task -> backend Student
    private static Student toStudent(Task t) {
        if (t == null) return null;

        int id = t.getId();

        // firstName and lastName are not in the UI, so we set simple placeholders.
        String firstName = "Student";
        String lastName = "Task";

        String className = t.getCourse();
        String taskText = t.getTitle();
        String status = t.getStatus();

        return new Student(id, firstName, lastName, className, taskText, status);
    }

    // Update nextId based on maximum ID from Firebase
    private static void updateNextId(List<Student> students) {
        int max = nextId;
        if (students != null) {
            for (Student s : students) {
                if (s != null && s.getId() > max) {
                    max = s.getId();
                }
            }
        }
        nextId = max + 1;
    }
    // Get tasks whose due date is in the given month (used for reports)
    public static List<Task> getTasksForMonth(YearMonth month) {
        List<Task> result = new ArrayList<>();

        for (Task t : tasks) {
            if (t == null) continue;

            LocalDate d = t.getDueDate();
            if (d != null && YearMonth.from(d).equals(month)) {
                result.add(t);
            }
        }

        return result;
    }
}
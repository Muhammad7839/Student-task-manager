package com.studenttaskmanager.backend.app;

import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;
import com.studenttaskmanager.backend.repository.FirebaseStudentRepository;

import java.util.List;

/**
 * This class is only used to test the backend during development.
 * It shows that Firebase is connected correctly and that all CRUD
 * (Create, Read, Update, Delete) operations work as expected.
 *
 * The final UI will call the repository methods directly, but having
 * a simple test runner helps verify the backend independently.
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Start Firebase connection (loads serviceAccountKey.json)
        FirebaseConfig.init();

        // Step 2: Create the repository that talks to Firestore
        FirebaseStudentRepository repo = new FirebaseStudentRepository();

        // ---------------------------
        // Create sample student records
        // ---------------------------
        System.out.println("\n--- Adding Sample Students to Firebase ---");
        repo.addStudent(new Student(
                20001, "John", "Doe",
                "Math 101", "Homework 1", "Incomplete"
        ));
        repo.addStudent(new Student(
                20002, "Alice", "Smith",
                "Science 202", "Lab Report", "Incomplete"
        ));

        // ---------------------------
        // Read all students
        // ---------------------------
        System.out.println("\n--- Fetching All Students from Firebase ---");
        List<Student> students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        // ---------------------------
        // Update one record (mark task complete)
        // ---------------------------
        System.out.println("\n--- Updating Status for ID 20001 to 'Complete' ---");
        repo.updateStatus(20001, "Complete");

        // Read again after update
        System.out.println("\n--- Fetching After Update ---");
        students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        // ---------------------------
        // Delete one record
        // ---------------------------
        System.out.println("\n--- Deleting ID 20002 ---");
        repo.deleteStudent(20002);

        // Read again after delete
        System.out.println("\n--- Fetching After Delete ---");
        students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        // Done
        System.out.println("\nBackend test completed successfully.");
    }
}
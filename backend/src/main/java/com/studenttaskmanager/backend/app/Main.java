package com.studenttaskmanager.backend.app;

import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;
import com.studenttaskmanager.backend.repository.FirebaseStudentRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Initialize Firebase
        FirebaseConfig.init();

        // 2. Create repository that talks to Firestore
        FirebaseStudentRepository repo = new FirebaseStudentRepository();

        System.out.println("\n--- Adding Sample Students to Firebase ---");
        repo.addStudent(new Student(20001, "John", "Doe", "Math 101", "Homework 1", "Incomplete"));
        repo.addStudent(new Student(20002, "Alice", "Smith", "Science 202", "Lab Report", "Incomplete"));

        System.out.println("\n--- Fetching All Students from Firebase ---");
        List<Student> students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\n--- Updating Status for ID 20001 to 'Complete' ---");
        repo.updateStatus(20001, "Complete");

        System.out.println("\n--- Fetching After Update ---");
        students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\n--- Deleting ID 20002 ---");
        repo.deleteStudent(20002);

        System.out.println("\n--- Fetching After Delete ---");
        students = repo.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\nDone.");
    }
}
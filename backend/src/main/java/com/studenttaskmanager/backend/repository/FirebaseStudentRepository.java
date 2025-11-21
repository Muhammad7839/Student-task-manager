package com.studenttaskmanager.backend.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repository for handling CRUD operations for Student data using Firebase Firestore.
 * This is the primary backend for the final CSC325 project.
 */
public class FirebaseStudentRepository {

    private final Firestore db;
    private static final String COLLECTION = "students";

    public FirebaseStudentRepository() {
        this.db = FirebaseConfig.getFirestore();
    }

    /**
     * Adds or overwrites a student in Firestore.
     */
    public boolean addStudent(Student s) {
        try {
            ApiFuture<WriteResult> future = db.collection(COLLECTION)
                    .document(String.valueOf(s.getId()))
                    .set(s);
            future.get();
            System.out.println("Student added to Firebase with ID: " + s.getId());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to add student to Firebase");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all students from Firestore.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                Student s = doc.toObject(Student.class);
                list.add(s);
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch students from Firebase");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Updates the "status" field of a student document.
     */
    public boolean updateStatus(int id, String newStatus) {
        try {
            ApiFuture<WriteResult> future = db.collection(COLLECTION)
                    .document(String.valueOf(id))
                    .update("status", newStatus);

            future.get();
            System.out.println("Status updated for student id: " + id);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to update status in Firebase");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a student document from Firestore.
     */
    public boolean deleteStudent(int id) {
        try {
            ApiFuture<WriteResult> future = db.collection(COLLECTION)
                    .document(String.valueOf(id))
                    .delete();

            future.get();
            System.out.println("Student deleted from Firebase id: " + id);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to delete student from Firebase");
            e.printStackTrace();
            return false;
        }
    }
}
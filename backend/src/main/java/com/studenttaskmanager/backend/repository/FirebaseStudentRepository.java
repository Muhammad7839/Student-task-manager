package com.studenttaskmanager.backend.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.studenttaskmanager.backend.db.FirebaseConfig;
import com.studenttaskmanager.backend.models.Student;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that handles all CRUD operations for Student objects
 * using Firebase Cloud Firestore as the backend database.
 *
 * This is the main way the rest of the application talks to the database.
 */
public class FirebaseStudentRepository {

    // Firestore database reference.
    private final Firestore db;

    // Name of the collection that stores all student task records.
    private static final String COLLECTION = "students";

    /**
     * Constructor gets a Firestore instance from FirebaseConfig.
     */
    public FirebaseStudentRepository() {
        this.db = FirebaseConfig.getFirestore();
    }

    /**
     * Adds a new student or overwrites an existing student document in Firestore.
     * Also sets createdAt (first time) and always updates updatedAt.
     *
     * @param s student object to be saved
     * @return true if write succeeded, false otherwise
     */
    public boolean addStudent(Student s) {
        try {
            // Set timestamps using ISO-8601 format for readability.
            String now = Instant.now().toString();

            if (s.getCreatedAt() == null || s.getCreatedAt().isEmpty()) {
                s.setCreatedAt(now);
            }
            s.setUpdatedAt(now);

            ApiFuture<WriteResult> future = db.collection(COLLECTION)
                    .document(String.valueOf(s.getId()))
                    .set(s);

            // Block until the write completes (simple approach for this project).
            future.get();

            System.out.println("Student added to Firebase with ID: " + s.getId());
            return true;

        } catch (Exception e) {
            System.out.println("Failed to add student to Firebase.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all students stored in the Firestore "students" collection.
     *
     * @return list of Student objects (may be empty, but never null)
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
            System.out.println("Failed to fetch students from Firebase.");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Updates only the "status" (and updatedAt) field of a student document.
     *
     * @param id        student id
     * @param newStatus new status value (for example, "Complete" or "Incomplete")
     * @return true if update succeeded, false otherwise
     */
    public boolean updateStatus(int id, String newStatus) {
        try {
            String now = Instant.now().toString();

            ApiFuture<WriteResult> future = db.collection(COLLECTION)
                    .document(String.valueOf(id))
                    .update("status", newStatus,
                            "updatedAt", now);

            future.get();
            System.out.println("Status updated for student id: " + id);
            return true;

        } catch (Exception e) {
            System.out.println("Failed to update status in Firebase.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a student document from Firestore.
     *
     * @param id id of the student to delete
     * @return true if delete succeeded, false otherwise
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
            System.out.println("Failed to delete student from Firebase.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all students that belong to a specific className.
     * This is a small "extra" feature that shows how to filter in Firestore.
     *
     * @param className class name to filter by (e.g. "Math 101")
     * @return list of matching Student objects
     */
    public List<Student> getStudentsByClassName(String className) {
        List<Student> list = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION)
                    .whereEqualTo("className", className)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                list.add(doc.toObject(Student.class));
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch students by className from Firebase.");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Returns all students with a given status.
     * Example: "Incomplete" or "Complete".
     *
     * @param status status value to filter by
     * @return list of matching Student objects
     */
    public List<Student> getStudentsByStatus(String status) {
        List<Student> list = new ArrayList<>();

        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION)
                    .whereEqualTo("status", status)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                list.add(doc.toObject(Student.class));
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch students by status from Firebase.");
            e.printStackTrace();
        }

        return list;
    }
}
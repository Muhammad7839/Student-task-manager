package com.studenttaskmanager.backend.db;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Initializes the Firebase Admin SDK and provides a Firestore instance.
 * This class is used by repository classes that talk to Firebase.
 */
public class FirebaseConfig {

    // We only want to initialize Firebase once.
    private static boolean initialized = false;

    /**
     * Initializes Firebase using the serviceAccountKey.json in resources.
     * If Firebase is already initialized, this method does nothing.
     */
    public static void init() {
        if (initialized) {
            return;
        }

        try (InputStream serviceAccount =
                     FirebaseConfig.class.getClassLoader()
                             .getResourceAsStream("serviceAccountKey.json")) {

            if (serviceAccount == null) {
                throw new IllegalStateException(
                        "serviceAccountKey.json not found in resources folder.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;
            System.out.println("Firebase initialized successfully.");

        } catch (IOException e) {
            System.out.println("Failed to initialize Firebase.");
            e.printStackTrace();
        }
    }

    /**
     * Returns a Firestore instance. Automatically initializes Firebase if needed.
     */
    public static Firestore getFirestore() {
        if (!initialized) {
            init();
        }
        return FirestoreClient.getFirestore();
    }
}
package com.studenttaskmanager.backend.db;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for initializing the Firebase Admin SDK
 * and providing access to the Firestore database.
 *
 * It loads the serviceAccountKey.json file from the resources folder
 * and creates the Firebase application instance only once.
 *
 * The repository classes call getFirestore() whenever they need to
 * communicate with the Firestore database.
 */
public class FirebaseConfig {

    // Ensures Firebase is only initialized once for the entire backend.
    private static boolean initialized = false;

    /**
     * Initializes Firebase using the service account key stored in:
     *    backend/src/main/resources/serviceAccountKey.json
     *
     * If Firebase is already initialized, this method simply returns.
     */
    public static void init() {
        if (initialized) {
            return; // Already initialized, nothing to do.
        }

        try (InputStream serviceAccount =
                     FirebaseConfig.class.getClassLoader()
                             .getResourceAsStream("serviceAccountKey.json")) {

            if (serviceAccount == null) {
                throw new IllegalStateException(
                        "ERROR: serviceAccountKey.json not found in resources folder."
                );
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;
            System.out.println("Firebase initialized successfully.");

        } catch (IOException e) {
            System.out.println("Failed to initialize Firebase (I/O error).");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error while initializing Firebase.");
            e.printStackTrace();
        }
    }

    /**
     * Returns a Firestore instance.
     * If Firebase was not initialized before this method is called,
     * it will initialize automatically.
     *
     * @return Firestore database instance
     */
    public static Firestore getFirestore() {
        if (!initialized) {
            init();
        }
        return FirestoreClient.getFirestore();
    }
}
package com.studenttaskmanager.backend;

import com.google.cloud.firestore.Firestore;
import com.studenttaskmanager.backend.db.FirebaseConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration-style test to verify that FirebaseConfig
 * can initialize Firebase and return a non-null Firestore instance.
 *
 * This test assumes that serviceAccountKey.json is present in
 * src/main/resources and that the Firebase project is configured.
 */
public class FirebaseConfigTest {

    @Test
    public void testGetFirestoreNotNull() {
        FirebaseConfig.init();
        Firestore firestore = FirebaseConfig.getFirestore();
        assertNotNull(firestore, "Firestore instance should not be null after init()");
    }
}
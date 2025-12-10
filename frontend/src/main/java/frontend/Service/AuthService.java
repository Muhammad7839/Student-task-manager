package frontend.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthService provides basic in-memory authentication
 * for registering and validating users.
 *
 * This version does NOT persist data. All registered users
 * are stored only while the app is running. It is meant as
 * a placeholder until a real backend or database is added.
 */
public class AuthService {

    /**
     * Internal map storing username → password.
     * This acts as a temporary user database.
     */
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        // Pre-loaded demo account for testing login.
        USERS.put("demo", "demo123");
    }

    /**
     * Registers a new user into the in-memory user store.
     *
     * @param username the username to register
     * @param password the password for the new user
     * @return true if registration succeeded, false if data is invalid
     *         or the username already exists
     */
    public static boolean register(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            return false;
        }

        synchronized (USERS) {
            if (USERS.containsKey(username)) {
                // Username already taken
                return false;
            }
            USERS.put(username, password);
            return true;
        }
    }

    /**
     * Authenticates a user by checking if the username exists
     * and the provided password matches the stored password.
     *
     * @param username the username attempting to log in
     * @param password the password entered by the user
     * @return true if authentication succeeded, false otherwise
     */
    public static boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedPassword;
        synchronized (USERS) {
            storedPassword = USERS.get(username);
        }

        return storedPassword != null && storedPassword.equals(password);
    }

    /**
     * Returns an unmodifiable view of all registered users.
     * Mainly for debugging or testing.
     *
     * @return map containing all usernames and their passwords
     */
    public static Map<String, String> getAllUsers() {
        synchronized (USERS) {
            return Collections.unmodifiableMap(new HashMap<>(USERS));
        }
    }
}
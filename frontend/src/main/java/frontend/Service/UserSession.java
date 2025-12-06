package frontend.Service;

/**
 * Holds simple in-memory user session data for the currently logged-in user.
 * <p>
 * This class stores:
 *  - The user's display name,
 *  - The path to the user's profile image.
 * <p>
 * It also allows UI components (such as the sidebar) to register listeners
 * so they can automatically refresh when the name or image changes.
 *
 * Note: This is not persistent storage, and all values reset when the
 * application restarts or when {@link #clear()} is called.
 */
public class UserSession {

    /**
     * Display name shown in the UI, for example in the sidebar.
     */
    private static String displayName;

    /**
     * Absolute file path to the user's chosen profile image.
     */
    private static String profileImagePath;

    /**
     * Listener that gets triggered whenever the display name changes.
     */
    private static Runnable displayNameListener;

    /**
     * Listener that gets triggered whenever the profile image path changes.
     */
    private static Runnable profileImageListener;

    /**
     * Retrieves the current display name.
     *
     * @return the display name, or null if not set
     */
    public static String getDisplayName() {
        return displayName;
    }

    /**
     * Updates the display name for this session.
     * <p>
     * Any registered listener will be triggered immediately.
     *
     * @param name the new display name
     */
    public static void setDisplayName(String name) {
        displayName = name;
        if (displayNameListener != null) {
            displayNameListener.run();
        }
    }

    /**
     * Returns the currently stored path to the user's profile photo.
     *
     * @return a file path string, or null if none is set
     */
    public static String getProfileImagePath() {
        return profileImagePath;
    }

    /**
     * Updates the profile image path and notifies listeners.
     *
     * @param path absolute path to the selected image
     */
    public static void setProfileImagePath(String path) {
        profileImagePath = path;
        if (profileImageListener != null) {
            profileImageListener.run();
        }
    }

    /**
     * Registers a listener that runs whenever the display name changes.
     *
     * @param listener a Runnable callback triggered on name update
     */
    public static void setDisplayNameListener(Runnable listener) {
        displayNameListener = listener;
    }

    /**
     * Registers a listener that runs whenever the profile image changes.
     *
     * @param listener a Runnable callback triggered on image update
     */
    public static void setProfileImageListener(Runnable listener) {
        profileImageListener = listener;
    }

    /**
     * Clears all session data and listeners.
     * <p>
     * Called when a user logs out or when the application resets session state.
     */
    public static void clear() {
        displayName = null;
        profileImagePath = null;
        displayNameListener = null;
        profileImageListener = null;
    }
}

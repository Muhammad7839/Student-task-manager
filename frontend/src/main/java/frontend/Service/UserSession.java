package frontend.Service;

public class UserSession {

    private static String displayName;
    private static String profileImagePath;

    private static Runnable displayNameListener;
    private static Runnable profileImageListener;

    public static String getDisplayName() {
        return displayName;
    }

    public static void setDisplayName(String name) {
        displayName = name;
        if (displayNameListener != null) {
            displayNameListener.run();
        }
    }

    public static String getProfileImagePath() {
        return profileImagePath;
    }

    public static void setProfileImagePath(String path) {
        profileImagePath = path;
        if (profileImageListener != null) {
            profileImageListener.run();
        }
    }

    public static void setDisplayNameListener(Runnable listener) {
        displayNameListener = listener;
    }

    public static void setProfileImageListener(Runnable listener) {
        profileImageListener = listener;
    }

    public static void clear() {
        displayName = null;
        profileImagePath = null;
        displayNameListener = null;
        profileImageListener = null;
    }
}
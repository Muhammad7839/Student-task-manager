package frontend;

import frontend.Service.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX application entry point.
 *
 * This class handles:
 *  - initial startup of the program
 *  - loading the first screen (Login)
 *  - global scene management
 *  - switching between screens without losing the active theme
 *
 * The entire frontend uses ONE shared Scene, and only the root node changes.
 * This makes theme switching simpler and avoids creating new windows.
 */
public class MainApp extends Application {

    /** Main window reference so all controllers can request screen changes. */
    private static Stage primaryStage;

    /**
     * Allows other classes (like controllers) to access the main window.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Called when the JavaFX app starts.
     * Sets up the primary stage, loads the login screen,
     * attaches the main stylesheet, and applies the default theme.
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent root = FXMLLoader.load(
                MainApp.class.getResource("/frontend/fxml/Login.fxml"));

        Scene scene = new Scene(root);

        // Attach the main CSS file (only one global stylesheet)
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
                MainApp.class.getResource("/frontend/css/styles.css").toExternalForm()
        );

        // Start the application in light mode
        ThemeManager.applyTheme(root, ThemeManager.Theme.LIGHT);

        primaryStage.setTitle("Student Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Replaces the root of the current Scene with a new FXML layout.
     * Keeps the same Scene and reapplies the active theme.
     *
     * @param fxmlPath path to the new FXML file inside /resources/frontend/fxml/
     */
    private static void setRoot(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(fxmlPath));

            Scene scene = primaryStage.getScene();

            // If the scene does not exist yet (should not happen normally)
            if (scene == null) {
                scene = new Scene(root);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        MainApp.class.getResource("/frontend/css/styles.css").toExternalForm()
                );
                primaryStage.setScene(scene);
            } else {
                // Swap the root node but keep the Scene
                scene.setRoot(root);
            }

            // Reapply the active theme so the new screen matches the previous one
            ThemeManager.applyTheme(root, ThemeManager.getCurrentTheme());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------
    //          Navigation
    // ------------------------------

    /** Show the dashboard screen. */
    public static void showDashboard() { setRoot("/frontend/fxml/Dashboard.fxml"); }

    /** Show the tasks screen. */
    public static void showTasks() { setRoot("/frontend/fxml/Tasks.fxml"); }

    /** Show the calendar screen. */
    public static void showCalendar() { setRoot("/frontend/fxml/Calendar.fxml"); }

    /** Show the analytics screen. */
    public static void showAnalytics() { setRoot("/frontend/fxml/Analytics.fxml"); }

    /** Show the settings screen. */
    public static void showSettings() { setRoot("/frontend/fxml/Settings.fxml"); }

    /** Show the login screen. */
    public static void showLogin() { setRoot("/frontend/fxml/Login.fxml"); }

    /** Show the signup screen. */
    public static void showSignup() { setRoot("/frontend/fxml/Signup.fxml"); }

    /**
     * Standard JavaFX entry point.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

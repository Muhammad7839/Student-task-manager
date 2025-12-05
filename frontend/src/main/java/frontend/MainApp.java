package frontend;

import frontend.Service.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent root = FXMLLoader.load(
                MainApp.class.getResource("/frontend/fxml/Login.fxml"));

        Scene scene = new Scene(root);

        // ONLY ONE stylesheet – styles.css
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
                MainApp.class.getResource("/frontend/css/styles.css").toExternalForm()
        );

        // start in LIGHT theme
        ThemeManager.applyTheme(root, ThemeManager.Theme.LIGHT);

        primaryStage.setTitle("Student Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper to switch screens but keep the same Scene and theme
    private static void setRoot(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource(fxmlPath));

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(
                        MainApp.class.getResource("/frontend/css/styles.css").toExternalForm()
                );
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            // re-apply current theme to the new root
            ThemeManager.applyTheme(root, ThemeManager.getCurrentTheme());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigation helpers
    public static void showDashboard() {
        setRoot("/frontend/fxml/Dashboard.fxml");
    }

    public static void showTasks() {
        setRoot("/frontend/fxml/Tasks.fxml");
    }

    public static void showCalendar() {
        setRoot("/frontend/fxml/Calendar.fxml");
    }

    public static void showAnalytics() {
        setRoot("/frontend/fxml/Analytics.fxml");
    }

    public static void showSettings() {
        setRoot("/frontend/fxml/Settings.fxml");
    }

    public static void showLogin() {
        setRoot("/frontend/fxml/Login.fxml");
    }

    public static void showSignup() {
        setRoot("/frontend/fxml/Signup.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
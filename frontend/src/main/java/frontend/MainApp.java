package frontend;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("Student Task Manager");

        showSplash();
    }

    private void showSplash() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/frontend/fxml/Splash.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 680);
        scene.getStylesheets().add(
                getClass().getResource("/frontend/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> showLogin());
        delay.play();
    }

    public static void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Login.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/SignUp.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Tasks.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Calendar.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load Calendar screen.");
        }
    }
    public static void showAnalytics() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Analytics.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load Analytics screen.");
        }
    }

    public static void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/Settings.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load Settings screen.");
        }
    }


    // new method for Add Task screen
    public static void showAddTask() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/AddTask.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load Add Task screen.");
        }
    }
    public static void showTaskDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/TaskDetails.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to load Task Details screen.");
        }
    }


    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}

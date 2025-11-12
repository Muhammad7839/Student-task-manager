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

        // 2-second splash → login
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

    public static void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/frontend/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    MainApp.class.getResource("/frontend/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
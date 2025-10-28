package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the splash screen first
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/frontend/fxml/Splash.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1000, 680); // fixed size for all screens
        scene.getStylesheets().add(
                getClass().getResource("/frontend/css/styles.css").toExternalForm()
        );

        stage.setTitle("Student Task Manager");
        stage.setScene(scene);
        stage.setResizable(false); // prevent resizing and keeps all screens the same
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

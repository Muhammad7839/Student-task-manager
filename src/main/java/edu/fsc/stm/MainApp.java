package edu.fsc.stm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(
                getClass().getResource("/edu/fsc/stm/fxml/Splash.fxml"));
        Scene scene = new Scene(fxml.load(), 800, 600);

        // global stylesheet
        scene.getStylesheets().add(
                getClass().getResource("/edu/fsc/stm/styles.css").toExternalForm()
        );

        stage.setTitle("Student Task Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

package frontend.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {

    @FXML
    ProgressBar progressBar;
    @FXML
    Label loadingLabel;
    @FXML
    private ImageView logoImage;

     double progress = 0.0;

    @FXML
    private void initialize() {
        // simple loading animation (~2 seconds)
        Timeline tl = new Timeline(
                new KeyFrame(Duration.millis(40), e -> {
                    progress = Math.min(1.0, progress + 0.02);
                    progressBar.setProgress(progress);

                    if (progress < 0.3) loadingLabel.setText("Loading assets…");
                    else if (progress < 0.6) loadingLabel.setText("Initializing UI…");
                    else if (progress < 0.9) loadingLabel.setText("Preparing dashboard…");
                    else loadingLabel.setText("Ready");
                })
        );
        tl.setCycleCount(50); // 50 * 40ms ≈ 2s
        tl.setOnFinished(e -> goToMainView());
        tl.play();
    }

    private void goToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/fxml/Login.fxml"));
            Scene nextScene = new Scene(loader.load());


            // keep your global css
            nextScene.getStylesheets().add(
                    getClass().getResource("/frontend/css/styles.css").toExternalForm()
            );

            // fade-in only, no containers, no root swapping
            nextScene.getRoot().setOpacity(0.0);
            Stage stage = (Stage) ((Node) progressBar).getScene().getWindow();
            stage.setScene(nextScene);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), nextScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (loadingLabel != null) loadingLabel.setText("Failed to load main view.");
        }
    }
}
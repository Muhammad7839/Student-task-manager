package edu.fsc.stm.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class SplashController {

    @FXML private ProgressBar progressBar;
    @FXML private Label loadingLabel;

    private double progress = 0.0;

    @FXML
    private void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(40), e -> {
                    progress = Math.min(1.0, progress + 0.02);
                    progressBar.setProgress(progress);

                    if (progress < 0.3)      loadingLabel.setText("Loading assets…");
                    else if (progress < 0.6) loadingLabel.setText("Initializing UI…");
                    else if (progress < 0.9) loadingLabel.setText("Connecting database…");
                    else                     loadingLabel.setText("Ready");
                })
        );
        timeline.setCycleCount(50); // ~2 seconds
        timeline.setOnFinished(e ->
                System.out.println("Splash animation finished – ready for next screen!")
        );
        timeline.play();
    }
}

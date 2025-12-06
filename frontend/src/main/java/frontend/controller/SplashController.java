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

/**
 * Controller for the splash screen shown when the app starts.
 * <p>
 * This screen shows a short loading animation with a progress bar
 * and status text, then switches to the Login view with a fade-in effect.
 */
public class SplashController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label loadingLabel;

    @FXML
    private ImageView logoImage;

    /**
     * Current progress value for the splash animation, between 0.0 and 1.0.
     */
    private double progress = 0.0;

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * Creates a {@link Timeline} that updates the progress bar and loading text
     * every 40 milliseconds for about 2 seconds, then moves to the main view.
     */
    @FXML
    private void initialize() {
        // Simple loading animation (~2 seconds)
        Timeline tl = new Timeline(
                new KeyFrame(Duration.millis(40), e -> {
                    progress = Math.min(1.0, progress + 0.02);
                    progressBar.setProgress(progress);

                    if (progress < 0.3) {
                        loadingLabel.setText("Loading assets…");
                    } else if (progress < 0.6) {
                        loadingLabel.setText("Initializing UI…");
                    } else if (progress < 0.9) {
                        loadingLabel.setText("Preparing dashboard…");
                    } else {
                        loadingLabel.setText("Ready");
                    }
                })
        );
        tl.setCycleCount(50); // 50 * 40ms ≈ 2 seconds total
        tl.setOnFinished(e -> goToMainView());
        tl.play();
    }

    /**
     * Loads and shows the main Login view after the splash finishes.
     * <p>
     * This method:
     *  - Loads Login.fxml,
     *  - Applies the global CSS stylesheet,
     *  - Sets up a fade-in transition on the new root node,
     *  - Replaces the splash scene on the same stage.
     */
    private void goToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/fxml/Login.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Keep your global CSS
            nextScene.getStylesheets().add(
                    getClass().getResource("/frontend/css/styles.css").toExternalForm()
            );

            // Fade-in on the new scene root
            nextScene.getRoot().setOpacity(0.0);
            Stage stage = (Stage) ((Node) progressBar).getScene().getWindow();
            stage.setScene(nextScene);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), nextScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (loadingLabel != null) {
                loadingLabel.setText("Failed to load main view.");
            }
        }
    }
}

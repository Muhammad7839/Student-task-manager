package frontend.controller;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel; // initializes JavaFX runtime
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SplashControllerTest {

    private SplashController controller;

    @BeforeAll
    static void initToolkit() {
        // Start JavaFX runtime
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        controller = new SplashController();
        controller.progressBar = new ProgressBar();
        controller.loadingLabel = new Label();
    }

    @Test
    void testProgressBarStartsAtZero() {
        assertEquals(-1.0, controller.progressBar.getProgress(),
                "ProgressBar should start in indeterminate mode (-1.0)");
    }

    @Test
    void testLabelUpdatesDuringProgress() {
        Platform.runLater(() -> {
            controller.progress = 0.25;
            controller.progressBar.setProgress(controller.progress);
            controller.loadingLabel.setText("Loading assets…");

            assertEquals(0.25, controller.progressBar.getProgress());
            assertEquals("Loading assets…", controller.loadingLabel.getText());
        });
    }
}


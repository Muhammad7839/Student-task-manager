package frontend.util;

import frontend.MainApp;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Utility class for showing small, temporary toast-style notifications
 * in the bottom-right corner of the application window.
 * <p>
 * The notifications automatically disappear after a short delay and
 * can be styled using the CSS classes:
 *  - "toast-container" for the outer box,
 *  - "toast-label" for the text label.
 */
public class NotificationUtil {

    /**
     * Shows a toast message for ~2.5 seconds.
     * <p>
     * The message is displayed at the bottom-right corner of the main window.
     *
     * @param message text to display inside the notification
     */
    public static void show(String message) {
        Window owner = MainApp.getPrimaryStage();
        if (owner == null) {
            return;
        }

        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);

        Label label = new Label(message);
        label.getStyleClass().add("toast-label");

        HBox box = new HBox(label);
        box.getStyleClass().add("toast-container");
        box.setPadding(new Insets(8));

        popup.getContent().add(box);

        // Show near bottom-right of the window
        popup.show(owner);
        double x = owner.getX() + owner.getWidth() - 320;
        double y = owner.getY() + owner.getHeight() - 120;
        popup.setX(x);
        popup.setY(y);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    /**
     * Convenience wrapper for displaying a success toast.
     * @param message success message
     */
    public static void showSuccess(String message) {
        show(message);
    }

    /**
     * Convenience wrapper for displaying an error toast.
     * @param message error message
     */
    public static void showError(String message) {
        show(message);
    }
}

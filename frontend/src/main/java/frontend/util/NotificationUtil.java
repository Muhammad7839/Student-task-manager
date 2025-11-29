package frontend.util;

import frontend.MainApp;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class NotificationUtil {

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

        // show near bottom right of the window
        popup.show(owner);
        double x = owner.getX() + owner.getWidth() - 320;
        double y = owner.getY() + owner.getHeight() - 120;
        popup.setX(x);
        popup.setY(y);

        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    public static void showSuccess(String message) {
        show(message);
    }

    public static void showError(String message) {
        show(message);
    }
}

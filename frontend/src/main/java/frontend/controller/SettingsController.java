package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsController {

    @FXML private ImageView profileImage;
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> themeChoice;

    @FXML
    private void initialize() {
        try {
            Image img = new Image(
                    getClass().getResourceAsStream("/frontend/images/profile_placeholder.png"));
            profileImage.setImage(img);
        } catch (Exception ignored) {}

        themeChoice.setValue("Light");
        nameField.setText("Dieunie");
    }

    @FXML
    private void handleChangePhoto() {
        System.out.println("Change photo clicked");
    }

    @FXML
    private void handleApplyTheme() {
        System.out.println("Theme applied: " + themeChoice.getValue());
    }

    @FXML
    private void handleLogout() {
        MainApp.showLogin();
    }
}

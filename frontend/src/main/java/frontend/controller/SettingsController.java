package frontend.controller;

import frontend.MainApp;
import frontend.Service.ThemeManager;
import frontend.Service.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class SettingsController {

    @FXML private ImageView profileImage;
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> themeChoice;

    @FXML
    private void initialize() {
        // load photo
        refreshPhoto();

        // load name
        String name = UserSession.getDisplayName();
        if (name == null) name = "";
        nameField.setText(name);

        // theme options
        if (themeChoice != null) {
            themeChoice.getItems().setAll("Light", "Dark");
            themeChoice.setValue(
                    ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK ? "Dark" : "Light"
            );
        }
    }

    private void refreshPhoto() {
        if (profileImage == null) return;

        String path = UserSession.getProfileImagePath();
        try {
            if (path != null && !path.isBlank() && new File(path).exists()) {
                profileImage.setImage(new Image(new File(path).toURI().toString()));
            } else {
                Image img = new Image(
                        getClass().getResourceAsStream("/frontend/images/profile_placeholder.png"));
                profileImage.setImage(img);
            }
        } catch (Exception ignored) {
        }
    }

    @FXML
    private void handleChangeName() {
        String raw = nameField.getText();
        String name = raw == null ? "" : raw.trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING) {{
                setTitle("Display name");
                setHeaderText(null);
                setContentText("Please enter a display name.");
            }}.showAndWait();
            return;
        }

        UserSession.setDisplayName(name);

        new Alert(Alert.AlertType.INFORMATION) {{
            setTitle("Display name");
            setHeaderText(null);
            setContentText("Name updated.");
        }}.showAndWait();
    }

    @FXML
    private void handleChangePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose profile photo");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = chooser.showOpenDialog(MainApp.getPrimaryStage());
        if (file == null) return;

        UserSession.setProfileImagePath(file.getAbsolutePath());
        refreshPhoto();
    }

    @FXML
    private void handleApplyTheme() {
        if (MainApp.getPrimaryStage() == null ||
                MainApp.getPrimaryStage().getScene() == null) {
            return;
        }

        String choice = themeChoice != null ? themeChoice.getValue() : "Light";
        ThemeManager.Theme theme =
                "Dark".equalsIgnoreCase(choice) ? ThemeManager.Theme.DARK : ThemeManager.Theme.LIGHT;

        ThemeManager.applyTheme(MainApp.getPrimaryStage().getScene().getRoot(), theme);

        new Alert(Alert.AlertType.INFORMATION) {{
            setTitle("Theme");
            setHeaderText(null);
            setContentText(theme == ThemeManager.Theme.DARK
                    ? "Dark theme applied."
                    : "Light theme applied.");
        }}.showAndWait();
    }

    @FXML
    private void handleLogout() {
        UserSession.clear();
        MainApp.showLogin();
    }
}
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

/**
 * Controller for the Settings screen.
 * <p>
 * This screen lets the user:
 *  - Change their display name,
 *  - Change their profile photo,
 *  - Switch between light and dark themes,
 *  - Log out of the current session.
 */
public class SettingsController {

    @FXML private ImageView profileImage;
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> themeChoice;

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * Initializes the settings view by:
     *  - Loading and showing the current profile photo,
     *  - Loading the saved display name,
     *  - Setting up the theme choice options and default selection.
     */
    @FXML
    private void initialize() {
        // Load profile photo from session or use placeholder
        refreshPhoto();

        // Load display name from session
        String name = UserSession.getDisplayName();
        if (name == null) name = "";
        nameField.setText(name);

        // Set up theme options and select current theme
        if (themeChoice != null) {
            themeChoice.getItems().setAll("Light", "Dark");
            themeChoice.setValue(
                    ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK ? "Dark" : "Light"
            );
        }
    }

    /**
     * Reloads the profile image from the path stored in {@link UserSession}.
     * <p>
     * If no valid photo is configured, a placeholder image is shown instead.
     */
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
            // If anything goes wrong, just keep whatever image is already set
        }
    }

    /**
     * Handles the "Change name" action.
     * <p>
     * Validates the input, updates the display name in {@link UserSession},
     * and shows a confirmation message.
     */
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

    /**
     * Handles the "Change photo" action.
     * <p>
     * Opens a file chooser so the user can pick an image file,
     * saves its path in {@link UserSession}, and refreshes the preview.
     */
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

    /**
     * Applies the selected theme to the whole application window.
     * <p>
     * Uses {@link ThemeManager} to switch between light and dark themes
     * and shows a small confirmation dialog.
     */
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

    /**
     * Logs out the current user.
     * <p>
     * Clears the {@link UserSession} and navigates back to the login screen.
     */
    @FXML
    private void handleLogout() {
        UserSession.clear();
        MainApp.showLogin();
    }
}

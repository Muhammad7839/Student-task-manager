package frontend.controller;

import frontend.MainApp;
import frontend.Service.ThemeManager;
import frontend.Service.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Controller for the left sidebar navigation.
 * <p>
 * This sidebar shows the user's profile photo and name,
 * provides navigation buttons to the main sections
 * (dashboard, tasks, calendar, reports, settings),
 * and includes a dark mode toggle and logout button.
 */
public class SidebarController {

    @FXML private VBox root;

    @FXML private ToggleButton homeBtn;
    @FXML private ToggleButton tasksBtn;
    @FXML private ToggleButton calendarBtn;
    @FXML private ToggleButton reportsBtn;
    @FXML private ToggleButton settingsBtn;

    @FXML private ImageView profileImage;
    @FXML private Label profileName;

    @FXML private Button darkToggleBtn;   // moon button
    @FXML private Button logoutBtn;

    /**
     * Called automatically after the FXML is loaded.
     * <p>
     * Initializes the profile name and photo, registers listeners so
     * changes in {@link UserSession} are reflected in the sidebar,
     * applies the correct background style for the current theme,
     * and wires the logout button.
     */
    @FXML
    private void initialize() {
        // Initial name and photo
        refreshProfileName();
        refreshProfileImage();

        // React when settings screen changes name or photo
        UserSession.setDisplayNameListener(this::refreshProfileName);
        UserSession.setProfileImageListener(this::refreshProfileImage);

        // Apply sidebar style based on current theme
        applySidebarTheme(ThemeManager.getCurrentTheme());

        // Logout button action
        if (logoutBtn != null) {
            logoutBtn.setOnAction(e -> handleLogout());
        }
    }

    /**
     * Updates the profile name label from {@link UserSession}.
     * <p>
     * If no name is stored, a default "Student" label is shown.
     */
    private void refreshProfileName() {
        if (profileName == null) return;

        String name = UserSession.getDisplayName();
        if (name == null || name.isBlank()) {
            name = "Student";
        }
        profileName.setText(name);
    }

    /**
     * Updates the profile image from the path stored in {@link UserSession}.
     * <p>
     * If there is no valid image path, a placeholder profile image is used.
     */
    private void refreshProfileImage() {
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
            // If loading fails, silently keep the current image
        }
    }

    /**
     * Applies inline styling to the sidebar background depending on the theme.
     * <p>
     * In dark mode, a solid dark color is used.
     * In light mode, we clear the style so the default CSS gradient is used.
     *
     * @param theme the current theme from {@link ThemeManager}
     */
    private void applySidebarTheme(ThemeManager.Theme theme) {
        if (root == null) return;

        if (theme == ThemeManager.Theme.DARK) {
            // Dark solid sidebar
            root.setStyle("-fx-background-color: #050816;");
        } else {
            // Remove inline style so the CSS gradient from stylesheet is used
            root.setStyle("");
        }
    }

    /**
     * Called from Sidebar.fxml: onAction="#onDarkToggleClicked".
     * <p>
     * Toggles the global theme using {@link ThemeManager#toggleTheme}
     * and then updates the sidebar background to match.
     */
    @FXML
    private void onDarkToggleClicked() {
        if (MainApp.getPrimaryStage() == null ||
                MainApp.getPrimaryStage().getScene() == null) {
            return;
        }

        // Toggle main root theme
        ThemeManager.toggleTheme(MainApp.getPrimaryStage().getScene().getRoot());

        // Refresh sidebar background to match new theme
        applySidebarTheme(ThemeManager.getCurrentTheme());
    }

    // -------- navigation --------

    /**
     * Navigates to the dashboard and marks the Home button as selected.
     */
    @FXML
    private void onHomeClicked() {
        selectOnly(homeBtn);
        MainApp.showDashboard();
    }

    /**
     * Navigates to the task list and marks the Tasks button as selected.
     */
    @FXML
    private void onTasksClicked() {
        selectOnly(tasksBtn);
        MainApp.showTasks();
    }

    /**
     * Navigates to the calendar and marks the Calendar button as selected.
     */
    @FXML
    private void onCalendarClicked() {
        selectOnly(calendarBtn);
        MainApp.showCalendar();
    }

    /**
     * Navigates to the analytics/reports screen and marks the Reports button as selected.
     */
    @FXML
    private void onReportsClicked() {
        selectOnly(reportsBtn);
        MainApp.showAnalytics();
    }

    /**
     * Navigates to the settings screen and marks the Settings button as selected.
     */
    @FXML
    private void onSettingsClicked() {
        selectOnly(settingsBtn);
        MainApp.showSettings();
    }

    /**
     * Marks exactly one toggle button as selected and clears the rest.
     *
     * @param active the button that should remain selected
     */
    private void selectOnly(ToggleButton active) {
        if (homeBtn != null)     homeBtn.setSelected(homeBtn == active);
        if (tasksBtn != null)    tasksBtn.setSelected(tasksBtn == active);
        if (calendarBtn != null) calendarBtn.setSelected(calendarBtn == active);
        if (reportsBtn != null)  reportsBtn.setSelected(reportsBtn == active);
        if (settingsBtn != null) settingsBtn.setSelected(settingsBtn == active);
    }

    // -------- logout --------

    /**
     * Logs the user out from the current session and returns to the login screen.
     * <p>
     * Also clears the display name and profile image path stored in {@link UserSession}.
     */
    private void handleLogout() {
        UserSession.setDisplayName(null);
        UserSession.setProfileImagePath(null);
        MainApp.showLogin();
    }
}

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

    @FXML
    private void initialize() {
        // initial name + photo
        refreshProfileName();
        refreshProfileImage();

        // react when settings screen changes name/photo
        UserSession.setDisplayNameListener(this::refreshProfileName);
        UserSession.setProfileImageListener(this::refreshProfileImage);

        // apply sidebar style based on current theme
        applySidebarTheme(ThemeManager.getCurrentTheme());

        // logout button
        if (logoutBtn != null) {
            logoutBtn.setOnAction(e -> handleLogout());
        }
    }

    private void refreshProfileName() {
        if (profileName == null) return;

        String name = UserSession.getDisplayName();
        if (name == null || name.isBlank()) {
            name = "Student";
        }
        profileName.setText(name);
    }

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
        } catch (Exception ignored) { }
    }

    private void applySidebarTheme(ThemeManager.Theme theme) {
        if (root == null) return;

        if (theme == ThemeManager.Theme.DARK) {
            // dark solid sidebar
            root.setStyle("-fx-background-color: #050816;");
        } else {
            // remove inline style so CSS gradient is used
            root.setStyle("");
        }
    }

    // called from Sidebar.fxml: onAction="#onDarkToggleClicked"
    @FXML
    private void onDarkToggleClicked() {
        if (MainApp.getPrimaryStage() == null ||
                MainApp.getPrimaryStage().getScene() == null) {
            return;
        }

        // toggle main root theme
        ThemeManager.toggleTheme(MainApp.getPrimaryStage().getScene().getRoot());

        // refresh sidebar background to match
        applySidebarTheme(ThemeManager.getCurrentTheme());
    }

    // -------- navigation --------

    @FXML
    private void onHomeClicked() {
        selectOnly(homeBtn);
        MainApp.showDashboard();
    }

    @FXML
    private void onTasksClicked() {
        selectOnly(tasksBtn);
        MainApp.showTasks();
    }

    @FXML
    private void onCalendarClicked() {
        selectOnly(calendarBtn);
        MainApp.showCalendar();
    }

    @FXML
    private void onReportsClicked() {
        selectOnly(reportsBtn);
        MainApp.showAnalytics();
    }

    @FXML
    private void onSettingsClicked() {
        selectOnly(settingsBtn);
        MainApp.showSettings();
    }

    private void selectOnly(ToggleButton active) {
        if (homeBtn != null)     homeBtn.setSelected(homeBtn == active);
        if (tasksBtn != null)    tasksBtn.setSelected(tasksBtn == active);
        if (calendarBtn != null) calendarBtn.setSelected(calendarBtn == active);
        if (reportsBtn != null)  reportsBtn.setSelected(reportsBtn == active);
        if (settingsBtn != null) settingsBtn.setSelected(settingsBtn == active);
    }

    // -------- logout --------

    private void handleLogout() {
        UserSession.setDisplayName(null);
        UserSession.setProfileImagePath(null);
        MainApp.showLogin();
    }
}
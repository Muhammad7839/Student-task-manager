package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class SidebarController {

    @FXML private VBox root;

    @FXML private ToggleButton homeBtn;
    @FXML private ToggleButton tasksBtn;
    @FXML private ToggleButton calendarBtn;
    @FXML private ToggleButton reportsBtn;
    @FXML private ToggleButton settingsBtn;

    @FXML private ImageView profileImage;
    @FXML private Label profileName;

    @FXML private Button darkToggleBtn;
    @FXML private FontIcon darkIcon;

    @FXML private Button logoutBtn;

    private boolean dark = false;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        homeBtn.setToggleGroup(group);
        tasksBtn.setToggleGroup(group);
        calendarBtn.setToggleGroup(group);
        reportsBtn.setToggleGroup(group);
        settingsBtn.setToggleGroup(group);
        homeBtn.setSelected(true);

        try {
            Image img = new Image(
                    getClass().getResourceAsStream("/frontend/images/profile_placeholder.png"));
            profileImage.setImage(img);
        } catch (Exception ignored) {
        }
        profileName.setText("Dieunie");

        darkToggleBtn.setOnAction(e -> toggleTheme());
        logoutBtn.setOnAction(e -> MainApp.showLogin());
    }

    private void toggleTheme() {
        if (root.getScene() == null) return;

        dark = !dark;

        var styles = root.getScene().getStylesheets();
        String darkCss = getClass()
                .getResource("/frontend/css/styles-dark.css")
                .toExternalForm();

        if (dark) {
            if (!styles.contains(darkCss)) {
                styles.add(darkCss);
            }
            darkIcon.setIconLiteral("fas-sun");
        } else {
            styles.remove(darkCss);
            darkIcon.setIconLiteral("fas-moon");
        }
    }

    @FXML
    private void onHomeClicked() {
        MainApp.showDashboard();
    }

    @FXML
    private void onTasksClicked() {
        MainApp.showTasks();
    }

    @FXML
    private void onCalendarClicked() {
        MainApp.showCalendar();
    }

    @FXML
    private void onReportsClicked() {
        MainApp.showAnalytics();
    }

    @FXML
    private void onSettingsClicked() {
        MainApp.showSettings();
    }
}

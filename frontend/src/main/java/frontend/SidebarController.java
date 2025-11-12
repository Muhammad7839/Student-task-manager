package frontend;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

public class SidebarController {

    // root, so we can reach the Scene to toggle dark class
    @FXML private VBox root;

    // nav
    @FXML private ToggleGroup navGroup;
    @FXML private ToggleButton homeBtn;
    @FXML private ToggleButton tasksBtn;
    @FXML private ToggleButton calendarBtn;
    @FXML private ToggleButton reportsBtn;
    @FXML private ToggleButton settingsBtn;

    // profile
    @FXML private ImageView profileImage;
    @FXML private Label profileName;
    @FXML private Button profileBtn;

    // theme
    @FXML private Button themeBtn;     // hooked to the moon/sun icon button
    @FXML private FontIcon themeIcon;  // the icon inside the button

    @FXML private Button logoutBtn;

    private boolean dark = false;

    @FXML
    private void initialize() {
        // ensure nav buttons are in the same ToggleGroup (safe even if set in FXML)
        if (navGroup == null) navGroup = new ToggleGroup();
        homeBtn.setToggleGroup(navGroup);
        tasksBtn.setToggleGroup(navGroup);
        calendarBtn.setToggleGroup(navGroup);
        reportsBtn.setToggleGroup(navGroup);
        settingsBtn.setToggleGroup(navGroup);

        // profile photo placeholder, clipped round
        try {
            Image img = new Image(getClass().getResourceAsStream("/frontend/images/profile_placeholder.png"));
            profileImage.setImage(img);
        } catch (Exception ignored) { }
        profileImage.setClip(new Circle(18, 18, 18));

        // when the scene appears, make sure we start in light mode
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) applyDark(newScene, false);
        });
    }

    @FXML
    private void toggleTheme() {
        Scene scene = root.getScene();
        if (scene == null) return;
        dark = !dark;
        applyDark(scene, dark);
        themeIcon.setIconLiteral(dark ? "fas-sun" : "fas-moon");
    }

    private void applyDark(Scene scene, boolean useDark) {
        // we keep a single stylesheet (styles.css) and toggle a style class on the root
        if (useDark) {
            if (!scene.getRoot().getStyleClass().contains("dark")) {
                scene.getRoot().getStyleClass().add("dark");
            }
        } else {
            scene.getRoot().getStyleClass().remove("dark");
        }
    }
}

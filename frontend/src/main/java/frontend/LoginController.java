package frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields.");
        } else {
            try {
                // Load dashboard.fxml after successful login
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/frontend/fxml/dashboard.fxml")
                );
                Scene dashboardScene = new Scene(loader.load(), 1000, 680);
                dashboardScene.getStylesheets().add(
                        getClass().getResource("/frontend/css/styles.css").toExternalForm()
                );

                // Switch to dashboard scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(dashboardScene);

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Failed to load dashboard.");
            }
        }
    }
}

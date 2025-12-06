package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login screen.
 * <p>
 * This class handles user input for username and password,
 * validates the fields, and navigates to the dashboard on success.
 * It also provides navigation to the signup screen if enabled.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink signupLink;

    /**
     * Called automatically when the FXML is loaded.
     * <p>
     * Ensures the login and signup controls work even if
     * the FXML does not explicitly set onAction handlers.
     */
    @FXML
    private void initialize() {
        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }
        if (signupLink != null) {
            signupLink.setOnAction(e -> handleSignupLink());
        }
    }

    /**
     * Validates the username and password fields.
     * <p>
     * If both fields are filled, the user is navigated to the dashboard.
     * Otherwise, an alert describes the missing information.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField != null && usernameField.getText() != null
                ? usernameField.getText().trim()
                : "";
        String password = passwordField != null && passwordField.getText() != null
                ? passwordField.getText().trim()
                : "";

        if (username.isEmpty()) {
            showError("Username is required.");
            return;
        }
        if (password.isEmpty()) {
            showError("Password is required.");
            return;
        }

        // Temporary behavior: accept any credentials and open dashboard
        MainApp.showDashboard();
    }

    /**
     * Handles clicking the signup link.
     * <p>
     * If the application has a signup screen configured,
     * this opens it. Otherwise, a simple message is shown.
     */
    @FXML
    private void handleSignupLink() {
        try {
            MainApp.showSignup();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup");
            alert.setHeaderText(null);
            alert.setContentText("Signup screen is not enabled in this build.");
            alert.showAndWait();
        }
    }

    /**
     * Displays a warning alert for a login validation issue.
     *
     * @param message the user-facing message explaining the problem
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login problem");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

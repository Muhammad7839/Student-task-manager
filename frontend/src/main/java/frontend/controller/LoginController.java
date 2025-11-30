package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink signupLink;

    @FXML
    private void initialize() {
        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }

        if (signupLink != null) {
            signupLink.setOnAction(e -> handleSignupLink());
        }
    }

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

        // username strict rules
        if (username.length() < 4) {
            showError("Username must be at least 4 characters long.");
            return;
        }
        if (!username.matches("[A-Za-z0-9]+")) {
            showError("Username can only contain letters and numbers.");
            return;
        }

        // password strict rules
        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }
        if (!password.matches(".*[A-Za-z].*")) {
            showError("Password must contain at least one letter.");
            return;
        }
        if (!password.matches(".*[0-9].*")) {
            showError("Password must contain at least one number.");
            return;
        }

        // later: real auth
        MainApp.showDashboard();
    }

    @FXML
    private void handleSignupLink() {
        MainApp.showSignup();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login problem");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

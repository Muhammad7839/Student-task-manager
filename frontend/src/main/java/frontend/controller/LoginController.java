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
        // make sure buttons work even if FXML has no onAction
        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }
        if (signupLink != null) {
            signupLink.setOnAction(e -> handleSignupLink());
        }
    }

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

        // for now: accept any username/password and go to dashboard
        MainApp.showDashboard();
    }

    @FXML
    private void handleSignupLink() {
        // if you have Signup.fxml wired, this will navigate to it
        try {
            MainApp.showSignup();
        } catch (Exception ex) {
            // fallback if signup screen not available
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup");
            alert.setHeaderText(null);
            alert.setContentText("Signup screen is not enabled in this build.");
            alert.showAndWait();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login problem");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
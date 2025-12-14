package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Hyperlink signupLink;

    @FXML
    void initialize() {
        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }
        // you can also rely on onAction in FXML for signupLink, but this is safe
        if (signupLink != null) {
            signupLink.setOnAction(e -> handleSignupLink());
        }
    }

    private void handleLogin() {
        String username = usernameField != null && usernameField.getText() != null
                ? usernameField.getText().trim()
                : "";
        String password = passwordField != null && passwordField.getText() != null
                ? passwordField.getText()
                : "";

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // later this is where you check real credentials
        // for now, any non-empty username and password passes
        MainApp.showDashboard();
    }

    @FXML
    private void handleSignupLink() {
        MainApp.showSignup();
    }
}

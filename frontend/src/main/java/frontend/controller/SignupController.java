package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class SignupController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField studentIdField;
    @FXML private ComboBox<String> majorCombo;
    @FXML private ComboBox<String> yearCombo;
    @FXML private Label errorLabel;
    @FXML private Button createAccountButton;
    @FXML private Hyperlink backToLoginLink;

    @FXML
    private void initialize() {
        // simple defaults
        if (majorCombo != null && majorCombo.getItems().isEmpty()) {
            majorCombo.getItems().addAll(
                    "Computer Science",
                    "Information Technology",
                    "Engineering",
                    "Business",
                    "Other"
            );
        }

        if (yearCombo != null && yearCombo.getItems().isEmpty()) {
            yearCombo.getItems().addAll(
                    "Freshman",
                    "Sophomore",
                    "Junior",
                    "Senior",
                    "Graduate"
            );
        }

        if (errorLabel != null) {
            errorLabel.setText("");
        }

        if (createAccountButton != null) {
            createAccountButton.setOnAction(e -> handleSignup());
        }

        if (backToLoginLink != null) {
            backToLoginLink.setOnAction(e -> goBackToLogin());
        }
    }

    @FXML
    private void handleSignup() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }

        String fullName   = safeText(fullNameField);
        String email      = safeText(emailField);
        String password   = safeText(passwordField);
        String confirm    = safeText(confirmPasswordField);
        String studentId  = safeText(studentIdField);
        String major      = majorCombo != null && majorCombo.getValue() != null
                ? majorCombo.getValue().trim() : "";
        String year       = yearCombo != null && yearCombo.getValue() != null
                ? yearCombo.getValue().trim() : "";

        // basic validation
        if (fullName.isEmpty()) {
            showError("Please enter your full name.");
            return;
        }
        if (email.isEmpty()) {
            showError("Please enter your email.");
            return;
        }
        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }
        if (password.isEmpty() || confirm.isEmpty()) {
            showError("Please enter and confirm your password.");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }
        if (studentId.isEmpty()) {
            showError("Please enter your student ID.");
            return;
        }
        if (major.isEmpty()) {
            showError("Please select your major.");
            return;
        }
        if (year.isEmpty()) {
            showError("Please select your year level.");
            return;
        }

        System.out.println("New signup:");
        System.out.println("  Name: " + fullName);
        System.out.println("  Email: " + email);
        System.out.println("  Student ID: " + studentId);
        System.out.println("  Major: " + major);
        System.out.println("  Year: " + year);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Account created");
        alert.setHeaderText(null);
        alert.setContentText("Your account has been created. You can log in now.");
        alert.showAndWait();

        goBackToLogin();
    }

    private void goBackToLogin() {
        MainApp.showLogin();
    }

    private String safeText(TextField field) {
        if (field == null || field.getText() == null) return "";
        return field.getText().trim();
    }

    private String safeText(PasswordField field) {
        if (field == null || field.getText() == null) return "";
        return field.getText().trim();
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        } else {
            System.out.println("Signup error: " + message);
        }
    }
}

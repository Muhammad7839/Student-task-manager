package frontend.controller;

import frontend.MainApp;
import frontend.Service.AuthService;
import frontend.Service.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller for the signup screen.
 * <p>
 * This screen lets a new student create an account by entering
 * their name, email, password, student ID, major, and year level.
 * It performs basic validation, stores the display name in {@link UserSession},
 * and then navigates back to the login screen.
 */
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

    /**
     * Called automatically when the FXML is loaded.
     * <p>
     * This method:
     *  - Populates the major and year drop-downs,
     *  - Clears any error text,
     *  - Wires the create-account and back-to-login actions.
     */
    @FXML
    private void initialize() {
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

    /**
     * Handles the create-account action.
     * <p>
     * Reads all form fields, validates them step by step, shows a clear
     * error message if something is wrong, and if everything is valid:
     *  - Registers the user with {@link AuthService} using email + password,
     *  - Saves the full name into {@link UserSession} as the display name,
     *  - Shows a simple confirmation dialog,
     *  - Returns to the login screen.
     */
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

        // Basic name checks
        if (fullName.isEmpty()) {
            showError("Please enter your full name.");
            return;
        }
        if (fullName.length() < 4) {
            showError("Full name must be at least 4 characters long.");
            return;
        }

        // Basic email checks
        if (email.isEmpty()) {
            showError("Please enter your email.");
            return;
        }
        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }

        // Password checks
        if (password.isEmpty() || confirm.isEmpty()) {
            showError("Please enter and confirm your password.");
            return;
        }

        // Simple strength rules
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
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        // Student and academic info
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

        // Register the account using email as the main login username
        boolean created = AuthService.register(email, password);
        if (!created) {
            showError("An account with this email already exists. Please use a different email or log in.");
            return;
        }

// Also register the full name as an alternate login name (username).
// If another student already has the same name, this might fail,
// but the email login will still work.
        AuthService.register(fullName, password);

// Save nicely formatted name into the session (e.g., "Muhammad Imran")
        UserSession.setDisplayName(fullName);

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

    /**
     * Navigates back to the login screen.
     * <p>
     * This is used both by the back link and after a successful signup.
     */
    private void goBackToLogin() {
        MainApp.showLogin();
    }

    /**
     * Safely extracts trimmed text from a {@link TextField}.
     * <p>
     * Returns an empty string if the field or its text is null.
     *
     * @param field the text field to read from
     * @return safe, trimmed text, never null
     */
    private String safeText(TextField field) {
        if (field == null || field.getText() == null) return "";
        return field.getText().trim();
    }

    /**
     * Safely extracts trimmed text from a {@link PasswordField}.
     * <p>
     * Returns an empty string if the field or its text is null.
     *
     * @param field the password field to read from
     * @return safe, trimmed text, never null
     */
    private String safeText(PasswordField field) {
        if (field == null || field.getText() == null) return "";
        return field.getText().trim();
    }

    /**
     * Shows a validation error message near the form.
     * <p>
     * If the error label is not available, logs the message to the console.
     *
     * @param message user-facing error message
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        } else {
            System.out.println("Signup error: " + message);
        }
    }
}
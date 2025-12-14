package frontend.controller;

import frontend.MainApp;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController controller;

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX runtime
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        controller = new LoginController();
        controller.usernameField = new TextField();
        controller.passwordField = new PasswordField();
        controller.loginButton = new Button();
        controller.signupLink = new Hyperlink();

        controller.initialize();
    }

    @Test
    void testLoginWithEmptyFieldsDoesNotShowDashboard() {
        controller.usernameField.setText("");
        controller.passwordField.setText("");

        try (MockedStatic<MainApp> mocked = mockStatic(MainApp.class)) {
            controller.loginButton.fire(); // simulate click
            mocked.verify(() -> MainApp.showDashboard(), never());
        }
    }

    @Test
    void testLoginWithValidCredentialsShowsDashboard() {
        controller.usernameField.setText("user");
        controller.passwordField.setText("pass");

        try (MockedStatic<MainApp> mocked = mockStatic(MainApp.class)) {
            controller.loginButton.fire(); // simulate click
            mocked.verify(() -> MainApp.showDashboard(), times(1));
        }
    }

    @Test
    void testSignupLinkShowsSignup() {
        try (MockedStatic<MainApp> mocked = mockStatic(MainApp.class)) {
            controller.signupLink.fire(); // simulate click
            mocked.verify(() -> MainApp.showSignup(), times(1));
        }
    }
}


package frontend.Service;

import javafx.scene.Parent;

public class ThemeManager {

    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void applyTheme(Parent root, Theme theme) {
        if (root == null) return;

        root.getStyleClass().remove("dark");

        if (theme == Theme.DARK) {
            root.getStyleClass().add("dark");
        }

        currentTheme = theme;
    }

    public static void toggleTheme(Parent root) {
        if (currentTheme == Theme.LIGHT) {
            applyTheme(root, Theme.DARK);
        } else {
            applyTheme(root, Theme.LIGHT);
        }
    }
}
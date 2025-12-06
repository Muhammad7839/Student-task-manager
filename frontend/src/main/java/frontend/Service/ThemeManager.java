package frontend.Service;

import javafx.scene.Parent;

/**
 * Utility class for managing the application's theme (light or dark).
 * <p>
 * This class works by adding or removing a "dark" CSS style class on the
 * root node of the current scene. All stylesheets that support dark mode
 * must include CSS rules scoped under the ".dark" selector.
 */
public class ThemeManager {

    /**
     * Available themes supported by the UI.
     */
    public enum Theme {
        LIGHT,
        DARK
    }

    /**
     * Tracks the currently active theme so the UI can toggle correctly.
     * Defaults to LIGHT.
     */
    private static Theme currentTheme = Theme.LIGHT;

    /**
     * Returns the theme that is currently active.
     *
     * @return the current theme
     */
    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Applies the selected theme to the given UI root node.
     * <p>
     * Implementation detail:
     *  - Removes any existing "dark" style class,
     *  - Adds the "dark" class only if the selected theme is DARK.
     *
     * @param root  the root node of the scene to apply styling to
     * @param theme the theme that should be applied
     */
    public static void applyTheme(Parent root, Theme theme) {
        if (root == null) return;

        // Remove previous theme marker class
        root.getStyleClass().remove("dark");

        // Add dark theme marker if needed
        if (theme == Theme.DARK) {
            root.getStyleClass().add("dark");
        }

        currentTheme = theme;
    }

    /**
     * Toggles between LIGHT and DARK themes.
     * <p>
     * This method calls {@link #applyTheme(Parent, Theme)} with the opposite
     * of the currently active theme.
     *
     * @param root the root UI node whose theme should be toggled
     */
    public static void toggleTheme(Parent root) {
        if (currentTheme == Theme.LIGHT) {
            applyTheme(root, Theme.DARK);
        } else {
            applyTheme(root, Theme.LIGHT);
        }
    }
}

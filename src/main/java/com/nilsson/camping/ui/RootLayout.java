package com.nilsson.camping.ui;

import com.nilsson.camping.ui.views.HomeView;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main container for the application.
 * Uses a BorderPane to hold the custom title bar (Top), the persistent side navigation (Left)
 * and the dynamic content (Center).
 */
public class RootLayout extends BorderPane {

    // Theme Constants
    private static final String DARK_THEME_CSS = "/dark-theme.css";
    private static final String LIGHT_THEME_CSS = "/light-theme.css";

    // Static variable to track the currently active theme URL globally
    private static String currentThemeUrl;

    /**
     * Public getter to allow UIUtil to apply the active theme to new windows (like Dialogs/Alerts).
     *
     * @return The absolute path URL string of the currently active stylesheet.
     */
    public static String getCurrentThemeUrl() {
        return currentThemeUrl;
    }

    // Instance variable to track the theme state
    private boolean isDarkTheme = true;

    /**
     * Constructor requires the main Stage reference, a logout callback, and the pre-initialized CustomTitleBar.
     *
     * @param stage    The main application Stage.
     * @param onLogout The Runnable to execute to switch back to the login screen.
     * @param titleBar The single, initialized CustomTitleBar instance.
     */
    public RootLayout(Stage stage, Runnable onLogout, CustomTitleBar titleBar) {
        // Apply the CSS class
        this.getStyleClass().add("root-layout");

        // Initialize the global theme tracker (This assumes MainApp loaded the dark theme initially)
        currentThemeUrl = getClass().getResource(DARK_THEME_CSS).toExternalForm();

        // Side Navigation (LEFT)
        SideNavigation sideNav = new SideNavigation(this, stage, onLogout);
        this.setLeft(sideNav);

        // Styling for the navigation width
        sideNav.setPrefWidth(250);

        // Default View (CENTER)
        setContent(new HomeView());
    }

    /**
     * Public method to swap the main content view.
     *
     * @param view The JavaFX Node to display.
     */
    public void setContent(Node view) {
        this.setCenter(view);
    }

    /**
     * Toggles the application stylesheet between dark-theme.css and light-theme.css.
     * Updates the global currentThemeUrl, and swaps the stylesheet on the Scene.
     *
     * @return true if the application is now using the Dark Theme, false otherwise.
     */
    public boolean toggleTheme() {
        if (this.getScene() == null) {
            System.err.println("Cannot toggle theme: Scene is null.");
            return isDarkTheme;
        }

        String oldTheme;
        String newTheme;

        if (isDarkTheme) {
            // Switching from Dark to Light
            oldTheme = getClass().getResource(DARK_THEME_CSS).toExternalForm();
            newTheme = getClass().getResource(LIGHT_THEME_CSS).toExternalForm();
            isDarkTheme = false;
        } else {
            // Switching from Light to Dark
            oldTheme = getClass().getResource(LIGHT_THEME_CSS).toExternalForm();
            newTheme = getClass().getResource(DARK_THEME_CSS).toExternalForm();
            isDarkTheme = true;
        }

        // Remove the old stylesheet
        this.getScene().getStylesheets().remove(oldTheme);

        // Add the new stylesheet
        if (!this.getScene().getStylesheets().contains(newTheme)) {
            this.getScene().getStylesheets().add(newTheme);
        }

        // Update the global tracker for dialogs/alerts
        currentThemeUrl = newTheme;

        return isDarkTheme;
    }
}
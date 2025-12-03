package com.nilsson.camping.app;

import com.nilsson.camping.service.SessionTimerService;
import com.nilsson.camping.ui.CustomTitleBar;
import com.nilsson.camping.ui.RootLayout;
import com.nilsson.camping.ui.views.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main entry point for the JavaFX application.
 */
public class MainApp extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private SessionTimerService sessionTimerService;
    private RootLayout rootLayout;
    private CustomTitleBar customTitleBar;
    private Runnable onLogout;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Removes Standard Title Bar
            primaryStage.initStyle(StageStyle.UNDECORATED);

            // Custom Title Bar - Pass the Runnable for 'X' button
            customTitleBar = new CustomTitleBar(primaryStage, this::handleCloseOrLogout);

            // Instantiate Session Timer and Static UserSession
            sessionTimerService = new SessionTimerService(customTitleBar);
            UserSession.initialize(customTitleBar);

            // Go back to Login View
            onLogout = () -> {
                UserSession.logout();
                showLoginView(primaryStage);
            };

            // Instantiate the single RootLayout
            rootLayout = new RootLayout(primaryStage, onLogout, customTitleBar);

            // Show the initial login view
            showLoginView(primaryStage);

            // Initial Scene Setup
            Scene scene = primaryStage.getScene();

            // CSS for styling
            String cssPath = getClass().getResource("/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            // Set the stage properties and show the application.
            primaryStage.setTitle("Wigell Camping - Login");
            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/icon.png").toExternalForm()));

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error loading resources or starting application:");
            e.printStackTrace();
        }
    }

    /**
     * Handles the clean shutdown logic when the user presses the 'X' button.
     */
    private void handleCloseOrLogout() {
        // Stop the Timer Thread
        if (sessionTimerService != null) {
            sessionTimerService.stop();
        }
        // Log out the user
        UserSession.logout();
        // Close the application
        if (getPrimaryStage() != null) {
            getPrimaryStage().close();
        }
    }

    /**
     * Shows the LoginView, wrapping it in a BorderPane with the CustomTitleBar.
     * This method is called both on initial start and after a logout.
     */
    private void showLoginView(Stage primaryStage) {

        LoginView newLoginView = new LoginView(primaryStage, rootLayout);

        BorderPane loginWrapper = new BorderPane();
        loginWrapper.setTop(customTitleBar);
        loginWrapper.setCenter(newLoginView);
        loginWrapper.getStyleClass().add("login-wrapper");

        if (primaryStage.getScene() != null) {
            primaryStage.getScene().setRoot(loginWrapper);
        } else {
            primaryStage.setScene(new Scene(loginWrapper, WIDTH, HEIGHT));
        }

        primaryStage.setTitle("Wigell Camping - Login");
    }

    // Helper to get the primaryStage reference
    private Stage getPrimaryStage() {
        return (Stage) customTitleBar.getScene().getWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.nilsson.camping.app;

import com.nilsson.camping.service.SessionTimerService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.nilsson.camping.ui.RootLayout;
import com.nilsson.camping.ui.views.LoginView;
import com.nilsson.camping.ui.CustomTitleBar;

/**
 * The main entry point for the JavaFX application.
 */
public class MainApp extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private SessionTimerService sessionTimerService;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Removes Standard Title Bar
            primaryStage.initStyle(StageStyle.UNDECORATED);

            RootLayout rootLayout;
            BorderPane loginWrapper;

            // Custom Title Bar - Pass the Runnable for 'X' button
            CustomTitleBar customTitleBar = new CustomTitleBar(primaryStage, () -> {

                // Stop the Timer Thread
                if (sessionTimerService != null) {
                    sessionTimerService.stop();
                }
                // Log out the user
                UserSession.logout();
            });

            // Instantiate Session Timer
            sessionTimerService = new SessionTimerService(customTitleBar);

            // Static UserSession/TimerService
            UserSession.initialize(customTitleBar);


            // onLogout Runnable - Stops the timer cleanly
            Runnable onLogout = () -> {
                // Ensure the timer is stopped and reset when logging out
                if (sessionTimerService != null) {
                    sessionTimerService.stop();
                }
                UserSession.logout();

                LoginView newLoginView = new LoginView(primaryStage, null);
                BorderPane newLoginWrapper = new BorderPane();

                newLoginWrapper.setTop(customTitleBar);
                newLoginWrapper.setCenter(newLoginView);

                primaryStage.getScene().setRoot(newLoginWrapper);
                primaryStage.setTitle("Wigell Camping - Login");
            };

            rootLayout = new RootLayout(primaryStage, onLogout, customTitleBar);
            
            // The LoginView gets the RootLayout instance it will switch to
            LoginView loginView = new LoginView(primaryStage, rootLayout);

            loginWrapper = new BorderPane();
            loginWrapper.setTop(customTitleBar);
            loginWrapper.setCenter(loginView);
            loginWrapper.getStyleClass().add("login-wrapper");

            Scene scene = new Scene(loginWrapper, WIDTH, HEIGHT);

            // CSS for styling
            String cssPath = getClass().getResource("/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            // Set the stage properties and show the application.
            primaryStage.setTitle("Wigell Camping - Login");
            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResource("/icon.png").toExternalForm()));
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error loading resources or starting application:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
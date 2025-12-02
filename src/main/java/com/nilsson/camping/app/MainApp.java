package com.nilsson.camping.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.nilsson.camping.ui.RootLayout;
import com.nilsson.camping.ui.views.LoginView;
import com.nilsson.camping.ui.CustomTitleBar;

//TODO
// ──────────────────────────────────────────────────────
//  The main entry point for the JavaFX application.
// ──────────────────────────────────────────────────────
public class MainApp extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Removes Standard Title Bar
            primaryStage.initStyle(StageStyle.UNDECORATED);

            RootLayout rootLayout;
            BorderPane loginWrapper;

            // Custom Title Bar
            CustomTitleBar customTitleBar = new CustomTitleBar(primaryStage);

            // Static UserSession/TimerService
            UserSession.initialize(customTitleBar);

            Runnable onLogout = () -> {
                // Ensure the timer is stopped and reset when logging out
                UserSession.logout();

                LoginView newLoginView = new LoginView(primaryStage, null);

                BorderPane newLoginWrapper = new BorderPane();
                // Use the single CustomTitleBar instance for the new login screen
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
package com.nilsson.camping.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.nilsson.camping.ui.RootLayout;
import com.nilsson.camping.app.UserSession;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.Ikon;
import javafx.scene.layout.AnchorPane;
import com.nilsson.camping.ui.CustomTitleBar;
import javafx.scene.layout.Pane;

/**
 * The initial screen of the application, used for user authentication.
 * It will swap the main content to RootLayout upon successful login.
 */
public class LoginView extends VBox {

    private final Stage primaryStage;

    public LoginView(Stage primaryStage, RootLayout rootLayout) {
        this.primaryStage = primaryStage;

        // Layout Setup for Centered Content
        VBox contentVBox = new VBox();
        contentVBox.setAlignment(Pos.CENTER);
        contentVBox.setSpacing(20);
        contentVBox.setPadding(new Insets(50));
        contentVBox.getStyleClass().add("login-view");

        // Main Logo Image Loading with Error Handling
        Label logo = new Label();
        Image logoImage = null;
        try {
            // Load the logo image resource
            logoImage = new Image(getClass().getResource("/logo.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading status image: " + "/logo.png" + ". Using text placeholder.");
        }

        if (logoImage != null) {
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(600);
            logoImageView.setPreserveRatio(true);
            logo.setGraphic(logoImageView);
        } else {
            // Fallback text if image fails to load
            logo.setText("Wigell Camping");
        }

        // Text Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (e.g., admin)");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password: (Use '0000')");

        // Wrap inputs with icons in containers
        HBox usernameContainer = createInputContainer(usernameField, FontAwesome.USER);
        HBox passwordContainer = createInputContainer(passwordField, FontAwesome.LOCK);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(100);
        loginButton.setDefaultButton(true);
        loginButton.setGraphic(new FontIcon(FontAwesome.SIGN_IN));

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setMaxWidth(100);
        exitButton.setOnAction(e -> {
            // Perform UserSession cleanup before closing
            UserSession.logout();
            primaryStage.close();
        });
        exitButton.setGraphic(new FontIcon(FontAwesome.POWER_OFF));

        // Add components to the VBox
        contentVBox.getChildren().addAll(logo, usernameContainer, passwordContainer, loginButton, exitButton);

        // Corner Image Component
        ImageView cornerImageView = new ImageView();
        Image cornerImage = null;
        try {
            cornerImage = new Image(getClass().getResource("/corner_logo.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading corner image: " + e.getMessage() + ". Check if '/corner_logo.png' exists.");
        }

        if (cornerImage != null) {
            cornerImageView.setImage(cornerImage);
            cornerImageView.setFitWidth(70);
            cornerImageView.setPreserveRatio(true);
        }

        // Container Setup for Positioning
        AnchorPane rootAnchorPane = new AnchorPane();
        rootAnchorPane.getChildren().add(contentVBox);

        AnchorPane.setTopAnchor(contentVBox, 0.0);
        AnchorPane.setBottomAnchor(contentVBox, 0.0);
        AnchorPane.setLeftAnchor(contentVBox, 0.0);
        AnchorPane.setRightAnchor(contentVBox, 0.0);

        // Add the corner image and anchor it to the bottom right
        if (cornerImage != null) {
            rootAnchorPane.getChildren().add(cornerImageView);

            AnchorPane.setBottomAnchor(cornerImageView, 40.0);
            AnchorPane.setRightAnchor(cornerImageView, 40.0);
        }

        this.getChildren().add(rootAnchorPane);
        VBox.setVgrow(rootAnchorPane, Priority.ALWAYS);


        // --- Action Handling (Temporary Bypass) ---
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            // String password = passwordField.getText(); // password is not used in the bypass logic

            // Always proceed to login:
            //if ("0000".equals(password))
            if (true) {
                // SUCCESS: Save user session data
                UserSession.login(username.isEmpty() ? "TestUser" : username); // Use TestUser if field is empty

                // Only switch if rootLayout is provided (i.e., this is the initial login)
                if (rootLayout != null) {
                    CustomTitleBar sharedTitleBar = UserSession.getInitializedTitleBar();

                    // 1. Detach the title bar from its current parent (the loginWrapper BorderPane)
                    if (sharedTitleBar != null && sharedTitleBar.getParent() != null) {
                        // We use the generic Pane type to ensure we can call getChildren()
                        ((Pane) sharedTitleBar.getParent()).getChildren().remove(sharedTitleBar);
                    }

                    // 2. FIX: Manually re-attach the title bar to the RootLayout instance
                    if (rootLayout.getTop() == null) {
                        rootLayout.setTop(sharedTitleBar);
                    }

                    // 3. Swap the scene root
                    primaryStage.getScene().setRoot(rootLayout);

                    // Updated title as requested
                    primaryStage.setTitle("Wigell Camping - Home");
                }
            }
        });
    }

    /**
     * Helper method to wrap a TextField/PasswordField and a FontIcon inside an HBox.
     *
     * @param field    The input control.
     * @param iconCode The Ikonli icon code.
     * @return An HBox containing the icon and the input field.
     */
    private HBox createInputContainer(TextField field, Ikon iconCode) {
        FontIcon icon = new FontIcon(iconCode);
        icon.setIconSize(20);

        icon.setStyle("-fx-font-family: 'FontAwesome';");

        // Create HBox
        HBox container = new HBox(6, icon, field);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(300);
        container.setPadding(new Insets(5, 0, 5, 0));

        HBox.setHgrow(field, Priority.ALWAYS);

        field.setMaxWidth(Double.MAX_VALUE);

        return container;
    }
}
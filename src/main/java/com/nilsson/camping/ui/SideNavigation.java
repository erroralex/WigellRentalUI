package com.nilsson.camping.ui;

import com.nilsson.camping.app.UserSession;
import com.nilsson.camping.ui.views.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

public class SideNavigation extends VBox {

    private final RootLayout rootLayout;
    private final Stage primaryStage;
    private final Runnable onLogout;

    private Button activeButton;
    private final VBox inventorySubMenu;
    private FontIcon inventoryToggleIcon;
    private final Button btnInventory;
    private FontIcon themeIcon;

    public SideNavigation(RootLayout rootLayout, Stage primaryStage, Runnable onLogout) {
        this.rootLayout = rootLayout;
        this.primaryStage = primaryStage;
        this.onLogout = onLogout;

        // Apply CSS class for the side navigation container
        this.getStyleClass().add("side-navigation");
        this.setSpacing(10);
        this.setPadding(new Insets(20));

        // Initialize the sub-menu VBox
        this.inventorySubMenu = new VBox(0);
        this.inventorySubMenu.setPadding(new Insets(0, 0, 0, 15)); // Indentation
        this.inventorySubMenu.getStyleClass().add("sub-menu");

        // ──────────────────────────────────────────────────────
        // Top Profile Area
        // ──────────────────────────────────────────────────────

        FontIcon userIcon = new FontIcon(FontAwesome.USER);
        userIcon.setIconSize(40);
        userIcon.getStyleClass().add("profile-icon");

        Label usernameLabel = new Label(UserSession.getCurrentUsername());

        VBox profileArea = new VBox(userIcon, usernameLabel);
        profileArea.getStyleClass().add("profile-area");
        profileArea.setAlignment(Pos.CENTER);

        profileArea.setSpacing(5);
        this.getChildren().add(profileArea);

        // ──────────────────────────────────────────────────────
        // Main Navigation Buttons
        // ──────────────────────────────────────────────────────

        // Home Button
        Button btnHome = createNavButton("Home", FontAwesome.HOME);
        btnHome.setOnAction(e -> {
            rootLayout.setContent(new HomeView());
            setActiveButton(btnHome);
        });

        // Members Button
        Button btnMembers = createNavButton("Members", FontAwesome.USERS);
        btnMembers.setOnAction(e -> {
            rootLayout.setContent(new MemberView());
            setActiveButton(btnMembers);
        });

        // Rentals Button
        Button btnRentals = createNavButton("Rentals", FontAwesome.PRODUCT_HUNT);
        btnRentals.setOnAction(e -> {
            rootLayout.setContent(new RentalView());
            setActiveButton(btnRentals);
        });

        // Profits Button
        Button btnProfits = createNavButton("Profits", FontAwesome.MONEY);
        btnProfits.setOnAction(e -> {
            rootLayout.setContent(new ProfitsView());
            setActiveButton(btnProfits);
        });

        // ──────────────────────────────────────────────────────
        // Inventory Parent Toggle Button (Collapsible)
        // ──────────────────────────────────────────────────────

        // Initialize and store the arrow icon
        FontIcon angleIcon = new FontIcon(FontAwesome.ANGLE_RIGHT); // Starts collapsed (ANGLE_RIGHT)
        this.inventoryToggleIcon = angleIcon; // Store the reference to the actual icon object

        // Create the button
        this.btnInventory = createToggleNavButton("Inventory", FontAwesome.CUBES, angleIcon);

        // Toggle Action
        btnInventory.setOnAction(e -> {
            // Toggle visibility of the sub-menu container
            boolean isVisible = inventorySubMenu.isVisible();
            inventorySubMenu.setVisible(!isVisible);
            inventorySubMenu.setManaged(!isVisible);

            // Toggle the arrow icon appearance
            if (inventorySubMenu.isVisible()) {
                this.inventoryToggleIcon.setIconLiteral(FontAwesome.ANGLE_DOWN.getDescription());
                btnInventory.getStyleClass().add("nav-button-toggle-active");
            } else {
                this.inventoryToggleIcon.setIconLiteral(FontAwesome.ANGLE_RIGHT.getDescription());
                btnInventory.getStyleClass().remove("nav-button-toggle-active");
            }
        });

        // ──────────────────────────────────────────────────────
        // Sub-Buttons (Vehicles and Gear)
        // ──────────────────────────────────────────────────────

        // Vehicle Sub-Button
        Button btnVehicle = createSubNavButton("Vehicles", FontAwesome.TRUCK);
        btnVehicle.setOnAction(e -> {
            rootLayout.setContent(new VehicleView());
            setActiveButton(btnVehicle);
        });

        // Gear Sub-Button
        Button btnGear = createSubNavButton("Gear", FontAwesome.GEAR);
        btnGear.setOnAction(e -> {
            rootLayout.setContent(new GearView());
            setActiveButton(btnGear);
        });

        // Add sub-buttons to the sub-menu container
        inventorySubMenu.getChildren().addAll(btnVehicle, btnGear);

        // Initially hide the sub-menu
        inventorySubMenu.setVisible(false);
        inventorySubMenu.setManaged(false);

        // ──────────────────────────────────────────────────────
        // Spacer for Bottom Alignment
        // ──────────────────────────────────────────────────────

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS); // This pushes everything below it to the bottom

        // ──────────────────────────────────────────────────────
        // Theme Toggle Button
        // ──────────────────────────────────────────────────────

        // Start in dark mode
        this.themeIcon = new FontIcon(FontAwesome.SUN_O);
        Button btnThemeToggle = createNavButton("Toggle Light-mode", this.themeIcon);

        btnThemeToggle.setOnAction(e -> {
            boolean isNowDark = rootLayout.toggleTheme();
            if (isNowDark) {
                // Dark mode
                this.themeIcon.setIconLiteral(FontAwesome.SUN_O.getDescription());
                btnThemeToggle.setText("Toggle Light-mode");
            } else {
                // Light mode
                this.themeIcon.setIconLiteral(FontAwesome.MOON_O.getDescription());
                btnThemeToggle.setText("Toggle Dark-mode");
            }
        });

        // Logout Button
        Button btnLogout = createNavButton("Logout", FontAwesome.SIGN_OUT);
        btnLogout.getStyleClass().add("btnExit");
        btnLogout.setOnAction(e -> {
            UserSession.logout();
            onLogout.run();
        });

        // Add to VBox
        this.getChildren().addAll(btnHome, btnMembers, btnInventory, inventorySubMenu, btnRentals, btnProfits, spacer, btnThemeToggle, btnLogout);

        // Set initial active button
        setActiveButton(btnHome);
    }

    // ──────────────────────────────────────────────────────
    // Helper Methods
    // ──────────────────────────────────────────────────────

    // Helper to create a standard navigation button
    private Button createNavButton(String text, Ikon iconCode) {
        Button btn = new Button(text);
        FontIcon icon = new FontIcon(iconCode);

        icon.setIconSize(20);
        icon.getStyleClass().add("nav-icon");

        btn.setGraphic(icon);

        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(10);
        return btn;
    }

    // Overloaded helper to create a standard navigation button using an existing FontIcon instance
    private Button createNavButton(String text, FontIcon icon) {
        Button btn = new Button(text);

        icon.setIconSize(20);
        icon.getStyleClass().add("nav-icon");

        btn.setGraphic(icon);

        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(10);
        return btn;
    }

    /**
     * Helper to create the toggle button (Inventory).
     * It combines the main icon with an arrow toggle indicator.
     *
     * @param toggleIcon The FontIcon instance used for the angle indicator.
     */
    private Button createToggleNavButton(String text, Ikon mainIconCode, FontIcon toggleIcon) {
        Button btn = new Button(text);

        // Main Icon
        FontIcon mainIcon = new FontIcon(mainIconCode);
        mainIcon.setIconSize(20);
        mainIcon.getStyleClass().add("nav-icon");

        // Toggle Icon
        toggleIcon.setIconSize(14);
        toggleIcon.getStyleClass().add("nav-icon-toggle");

        btn.setGraphic(UIUtil.createIconWithSpacer(mainIcon, toggleIcon));

        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(10);

        btn.getStyleClass().add("nav-button-toggle");

        return btn;
    }

    // Helper to create a sub-button (Vehicles/Gear)
    private Button createSubNavButton(String text, Ikon iconCode) {
        Button btn = createNavButton(text, iconCode);
        btn.getStyleClass().add("sub-nav-button");
        return btn;
    }

    // Sets the active state styling for the clicked button
    private void setActiveButton(Button newActiveButton) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("nav-button-active");
        }
        newActiveButton.getStyleClass().add("nav-button-active");
        activeButton = newActiveButton;

        // If a sub-button is clicked, ensure the parent toggle button shows a visual cue
        if (newActiveButton.getStyleClass().contains("sub-nav-button")) {
            // Check if the parent button is not already marked as active toggle
            if (!btnInventory.getStyleClass().contains("nav-button-toggle-active")) {
                btnInventory.getStyleClass().add("nav-button-toggle-active");
                // Ensure the arrow is down if one of its children is active
                this.inventoryToggleIcon.setIconLiteral(FontAwesome.ANGLE_DOWN.getDescription());
            }
        } else {
            // If a main button (Home, Members) is clicked, ensure the Inventory button
            // is NOT marked as active toggle
            if (activeButton != btnInventory) {
                btnInventory.getStyleClass().remove("nav-button-toggle-active");
                // Reset the arrow if it's not the active button
                if (this.inventoryToggleIcon.getIconLiteral().equals(FontAwesome.ANGLE_DOWN.getDescription())) {
                    this.inventoryToggleIcon.setIconLiteral(FontAwesome.ANGLE_RIGHT.getDescription());
                }
            }
        }
    }
}
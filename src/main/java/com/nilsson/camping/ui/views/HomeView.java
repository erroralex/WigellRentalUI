package com.nilsson.camping.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomeView extends VBox {

    public static final String WELCOME_MESSAGE =
            "Welcome to Wigell Camping Admin Portal\n" +
                    "\n" +
                    "You are logged in as an Administrator.\n" +
                    "\n" +
                    "The Wigell Camping Admin Portal is your centralized control panel for managing all aspects of the business. \n" +
                    "This application provides real-time oversight and tools necessary to maintain inventory, manage bookings, and \n" +
                    "ensure seamless operations.\n" +
                    "\n" +
                    "Key Administrative Capabilities:\n" +
                    "\n" +
                    "1. Inventory Management: View, add, edit, and retire vehicles (campers, motorhomes) and rental gear. \n" +
                    "Ensure all items are properly categorized and maintained with up-to-date details.\n" +
                    "\n" +
                    "2. Booking and Rental Oversight: Access a unified view of all current, upcoming, and past rental agreements. \n " +
                    "Quickly verify reservation status, adjust booking details, and track payment status.\n" +
                    "\n" +
                    "3. User Management: Maintain administrative and staff accounts, managing access levels and permissions to \n " +
                    "ensure system security and operational integrity.\n" +
                    "\n" +
                    "4. Reporting and Analytics: Generate reports on rental utilization, revenue tracking, and \n " +
                    "inventory availability to aid in strategic decision-making.\n" +
                    "\n" +
                    "Use the navigation sidebar to access the various modules and start managing your operations efficiently.";

    public HomeView() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_LEFT);

        // Logo Image Loading with Error Handling
        Label logo = new Label();
        Image logoImage = null;
        try {
            logoImage = new Image(getClass().getResource("/logo.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading status image: " + "/logo.png" + ". Using text placeholder.");
        }

        if (logoImage != null) {
            ImageView statusImageView = new ImageView(logoImage);
            statusImageView.setFitWidth(600);
            statusImageView.setPreserveRatio(true);
            logo.setGraphic(statusImageView);
            logo.setAlignment(Pos.TOP_CENTER);
        } else {
            // Fallback text if image fails to load
            logo.setText("Wigell Camping");
        }

        Label welcomeLabel = new Label(WELCOME_MESSAGE);

        this.getChildren().addAll(logo, welcomeLabel);
    }
}
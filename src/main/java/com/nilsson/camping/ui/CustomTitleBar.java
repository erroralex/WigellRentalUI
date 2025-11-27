package com.nilsson.camping.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Custom title bar component for an undecorated Stage.
 * Provides window dragging and control buttons (Minimize, Close).
 */
public class CustomTitleBar extends HBox {

    private double xOffset = 0;
    private double yOffset = 0;

    public CustomTitleBar(Stage stage) {

        // Apply CSS class
        this.getStyleClass().add("custom-title-bar");
        this.setAlignment(Pos.CENTER_LEFT);

        // Application Title
        Label titleLabel = new Label("Wigell Camping Admin Portal");
        titleLabel.getStyleClass().add("title-label"); // CSS class for golden color applied here

        // HBox spacer to push window controls to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Window Controls (Buttons)
        Button minimizeBtn = new Button("—");
        minimizeBtn.getStyleClass().add("window-button");
        minimizeBtn.setOnAction(e -> stage.setIconified(true));

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().addAll("window-button", "window-close");
        closeBtn.setOnAction(e -> stage.close());

        // Add components
        this.getChildren().addAll(titleLabel, spacer, minimizeBtn, closeBtn);

        // Window Dragging Logic
        // Record mouse press location
        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Move the stage based on mouse
        this.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
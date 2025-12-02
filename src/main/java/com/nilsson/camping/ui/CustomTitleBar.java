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

    // Timer components
    public Label timeDisplayLabel = new Label("00:00:00");
    private Label prefixLabel = new Label("Session Timer: ");
    private HBox timerContainer;

    public CustomTitleBar(Stage primarystage) {

        // Apply CSS class
        this.getStyleClass().add("custom-title-bar");
        this.setAlignment(Pos.CENTER_LEFT);

        // Application Title (Aligned Left)
        Label titleLabel = new Label("Wigell Camping Admin Portal");
        titleLabel.getStyleClass().add("title-label");

        // HBox spacer to push window controls to the right
        HBox leftSpacer = new HBox();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        // Session Timer Container (Aligned Center)
        prefixLabel.getStyleClass().add("timer-prefix"); // Optional CSS class
        timeDisplayLabel.getStyleClass().add("timer");

        timerContainer = new HBox(5, prefixLabel, timeDisplayLabel);
        timerContainer.setAlignment(Pos.CENTER);
        timerContainer.setMaxWidth(Double.MAX_VALUE);

        // Initially hide and unmanage the timer container
        timerContainer.setVisible(false);
        timerContainer.setManaged(false);


        // HBox spacer to balance the layout and push window controls to the right
        HBox rightSpacer = new HBox();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // Window Controls (Buttons)
        Button minimizeBtn = new Button("—");
        minimizeBtn.getStyleClass().add("window-button");
        minimizeBtn.setOnAction(e -> primarystage.setIconified(true));

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().addAll("window-button", "window-close");
        closeBtn.setOnAction(e -> primarystage.close());

        // Add components: Title | Left Spacer | Timer Container (Centered) | Right Spacer | Controls
        this.getChildren().addAll(titleLabel, leftSpacer, timerContainer, rightSpacer, minimizeBtn, closeBtn);

        // Window Dragging Logic with record mouse press location
        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Move the stage based on mouse
        this.setOnMouseDragged(event -> {
            primarystage.setX(event.getScreenX() - xOffset);
            primarystage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Toggles the visibility of the session timer container.
     * @param visible true to show the timer, false to hide it.
     */
    public void setTimerVisible(boolean visible) {
        // Must be run on the JavaFX application thread
        javafx.application.Platform.runLater(() -> {
            timerContainer.setVisible(visible);
            timerContainer.setManaged(visible);
        });
    }
}
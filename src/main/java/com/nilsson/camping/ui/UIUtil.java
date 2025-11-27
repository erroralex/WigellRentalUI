package com.nilsson.camping.ui;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility class for common UI operations, such as displaying standard alerts
 * and error messages, especially replacing the use of system alerts.
 */
public class UIUtil {

    private static final String CSS_PATH = "/dark-theme.css";

    /**
     * Helper method to apply the custom dark theme to any Alert dialog.
     */
    private static void applyTheme(Alert alert) {
        try {
            // Applies CSS file to the dialog's root container (DialogPane)
            String cssUrl = UIUtil.class.getResource(CSS_PATH).toExternalForm();
            alert.getDialogPane().getStylesheets().add(cssUrl);
        } catch (Exception e) {
            // Handle error if CSS file is not found
            System.err.println("Could not load CSS stylesheet: " + CSS_PATH);
            e.printStackTrace();
        }
    }

    /**
     * Shows a standard error alert dialog to the user.
     *
     * @param title The title of the alert window.
     * @param header The header text (main message).
     * @param content The detailed content message.
     */
    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyTheme(alert);

        alert.showAndWait();
    }

    /**
     * Shows an alert dialog containing the full stack trace of an exception.
     *
     * @param title The title of the alert window.
     * @param header The header text.
     * @param throwable The exception or throwable to display.
     */
    public static void showExceptionAlert(String title, String header, Throwable throwable) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(throwable.getMessage());

        applyTheme(alert);

        // Create expandable Exception details
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");
        label.getStyleClass().add("label");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.getStyleClass().addAll("text-field", "text-area");

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable content into the dialog
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    /**
     * Shows standard information alert dialog.
     *
     * @param title The title of the alert window.
     * @param header The header text.
     * @param content The detailed content message.
     */
    public static void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        applyTheme(alert);

        alert.showAndWait();
    }

    /**
     * Creates an HBox layout that pushes the main graphic (like a FontIcon) and the toggle icon
     * to the opposite sides of the button text space.
     * @param mainGraphic The main icon.
     * @param toggleGraphic The indicator icon.
     * @return An HBox containing the graphics with a spacer in between.
     */
    public static HBox createIconWithSpacer(Node mainGraphic, Node toggleGraphic) {
        // A Region node acts as a spacer that consumes all available space
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Container holding the main icon, the spacer, and the toggle icon
        HBox container = new HBox(mainGraphic, spacer, toggleGraphic);

        container.setMaxWidth(Double.MAX_VALUE);
        container.setSpacing(5); // Small gap between main icon and toggle icon

        return container;
    }
}
package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.items.RecreationalVehicle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Dialog for adding new Vehicles
public class AddVehicleDialog extends Dialog<RecreationalVehicle> {

    private final TextField makeField = new TextField();
    private final TextField modelField = new TextField();
    private final TextField yearField = new TextField();
    private final TextField capacityField = new TextField();
    private final TextField priceField = new TextField();
    private final ComboBox<String> typeBox = new ComboBox<>();

    private static final String CARAVAN = "Caravan";
    private static final String MOTORHOME = "Motorhome";
    private static final String CAMPERVAN = "Campervan";

    public AddVehicleDialog() {
        setTitle("Add New Recreational Vehicle");
        setHeaderText("Enter the details for the new vehicle.");

        // Remove Title bar
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        // CSS
        getDialogPane().getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
        getDialogPane().getStyleClass().add("add-entity-dialog");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Form Layout
        GridPane grid = createGridPane();

        // ComboBox
        typeBox.getItems().addAll(CARAVAN, CAMPERVAN, MOTORHOME);
        typeBox.setValue(CARAVAN);
        typeBox.setMaxWidth(Double.MAX_VALUE);

        // Grid layout
        grid.add(new Label("Make"), 0, 0);
        grid.add(makeField, 1, 0);
        makeField.setPromptText("(e.g., Kabe)");

        grid.add(new Label("Model"), 0, 1);
        grid.add(modelField, 1, 1);
        modelField.setPromptText("(e.g., Amethyst XXL)");

        grid.add(new Label("Type"), 0, 2);
        grid.add(typeBox, 1, 2);

        grid.add(new Label("Year"), 0, 3);
        grid.add(yearField, 1, 3);
        yearField.setPromptText("(e.g., 2023)");

        grid.add(new Label("Capacity"), 0, 4);
        grid.add(capacityField, 1, 4);
        capacityField.setPromptText("(e.g., 4 people)");

        grid.add(new Label("Daily Price (SEK)"), 0, 5);
        grid.add(priceField, 1, 5);
        priceField.setPromptText("(e.g., 1234)");

        getDialogPane().setContent(grid);

        // Focus
        Platform.runLater(makeField::requestFocus);

        // Enable/disable button
        Button addButton = (Button) getDialogPane().lookupButton(addButtonType);
        addButton.disableProperty().bind(
                makeField.textProperty().isEmpty()
                        .or(modelField.textProperty().isEmpty())
                        .or(yearField.textProperty().isEmpty())
                        .or(capacityField.textProperty().isEmpty())
                        .or(priceField.textProperty().isEmpty())
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new RecreationalVehicle(
                        makeField.getText().trim(),
                        modelField.getText().trim(),
                        typeBox.getValue(),
                        Double.parseDouble(priceField.getText().trim()),
                        yearField.getText().trim(),
                        capacityField.getText().trim()
                );
            }
            //If Cancel is clicked
            return null;
        });
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(20,20,10,10));
        return grid;
    }
}

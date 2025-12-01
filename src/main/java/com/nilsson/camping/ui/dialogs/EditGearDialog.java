package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.items.Gear;
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

// Dialog for adding new Gear
public class EditGearDialog extends Dialog<Gear> {

    private final TextField modelField = new TextField();
    private final TextField capacityField = new TextField();
    private final TextField priceField = new TextField();
    private final ComboBox<String> typeBox = new ComboBox<>();

    private static final String TENT = "Tent";
    private static final String BACKPACK = "Backpack";
    private static final String OTHER = "Other Gear";

    private final Gear gearToEdit;

    public EditGearDialog() {
        this(null);
    }

    public EditGearDialog(Gear gearToEdit) {

        this.gearToEdit = gearToEdit;

        setTitle("Edit Camping Gear");
        setHeaderText("Enter the details for " + gearToEdit.getModel());

        // Remove Title bar
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        // CSS
        getDialogPane().getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
        getDialogPane().getStyleClass().add("add-entity-dialog");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().clear();
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Form Layout
        GridPane grid = createGridPane();;

        // ComboBox
        typeBox.getItems().addAll(TENT, BACKPACK, OTHER);
        typeBox.setValue(gearToEdit.getType());
        typeBox.setMaxWidth(Double.MAX_VALUE);

        // Grid layout
        grid.add(new Label("Model Name"), 0, 0);
        grid.add(modelField, 1, 0);
        modelField.setText(gearToEdit.getModel());
        modelField.setPromptText("(e.g., Trail Trekker 2P)");

        grid.add(new Label("Type"), 0, 1);
        grid.add(typeBox, 1, 1);

        grid.add(new Label("Capacity"), 0, 2);
        grid.add(capacityField, 1, 2);
        capacityField.setText(gearToEdit.getCapacity());
        capacityField.setPromptText("(e.g., 2 people)");

        grid.add(new Label("Daily Price (SEK)"), 0, 3);
        grid.add(priceField, 1, 3);
        priceField.setText(String.valueOf(gearToEdit.getDailyPrice()));
        priceField.setPromptText("(e.g., 123)");

        getDialogPane().setContent(grid);

        // Focus
        Platform.runLater(modelField::requestFocus);

        // Enable/disable button
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(
                modelField.textProperty().isEmpty()
                        .or(capacityField.textProperty().isEmpty())
                        .or(priceField.textProperty().isEmpty())
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                gearToEdit.setModel(modelField.getText().trim());
                gearToEdit.setType(typeBox.getValue());
                gearToEdit.setCapacity(capacityField.getText().trim());
                gearToEdit.setDailyPrice(Double.parseDouble(priceField.getText().trim()));
                return gearToEdit;
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

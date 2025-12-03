package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.ui.UIUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddGearDialog extends Dialog<Gear> {

    private final TextField modelField = new TextField();
    private final TextField capacityField = new TextField();
    private final TextField priceField = new TextField();
    private final ComboBox<String> typeBox = new ComboBox<>();

    private static final String TENT = "Tent";
    private static final String BACKPACK = "Backpack";
    private static final String OTHER = "Other Gear";

    public AddGearDialog() {
        setTitle("Add New Camping Gear");
        setHeaderText("Enter the details for the new gear.");

        // Apply theme and mouse-drag
        this.setOnShowing(dialogEvent -> {
            UIUtil.applyDialogSetup(this);
        });

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Form Layout
        GridPane grid = createGridPane();;

        // ComboBox
        typeBox.getItems().addAll(TENT, BACKPACK, OTHER);
        typeBox.setValue(TENT);
        typeBox.setMaxWidth(Double.MAX_VALUE);

        // Grid layout
        grid.add(new Label("Model Name"), 0, 0);
        grid.add(modelField, 1, 0);
        modelField.setPromptText("(e.g., Trail Trekker 2P)");

        grid.add(new Label("Type"), 0, 1);
        grid.add(typeBox, 1, 1);

        grid.add(new Label("Capacity"), 0, 2);
        grid.add(capacityField, 1, 2);
        capacityField.setPromptText("(e.g., 2 people)");

        grid.add(new Label("Daily Price (SEK)"), 0, 3);
        grid.add(priceField, 1, 3);
        priceField.setPromptText("(e.g., 123)");

        getDialogPane().setContent(grid);

        // Focus
        Platform.runLater(modelField::requestFocus);

        // Enable/disable button
        Button addButton = (Button) getDialogPane().lookupButton(addButtonType);
        addButton.disableProperty().bind(
                modelField.textProperty().isEmpty()
                        .or(capacityField.textProperty().isEmpty())
                        .or(priceField.textProperty().isEmpty())
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Gear(
                        modelField.getText().trim(),
                        typeBox.getValue(),
                        capacityField.getText().trim(),
                        Double.parseDouble(priceField.getText().trim())
                        );
            }
            // If Cancel is clicked
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
package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.Vehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.ui.UIUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class GearView extends VBox {

    private final TableView<Gear> gearTable = new TableView<>();
    private final ObservableList<Gear> gearData = FXCollections.observableArrayList();

    public GearView() {

        // Apply CSS
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(gearTable, Priority.ALWAYS);

        Label title = new Label("Gear Management");
        title.getStyleClass().add("content-title");

        // TableView
        initializeTable();
        loadGearData();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, gearTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {

        // Model Column
        TableColumn<Gear, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(200);

        // Occupants Column
        TableColumn<Gear, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);

        // Capacity Column
        TableColumn<Gear, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.setPrefWidth(150);

        // Price Column
        TableColumn<Gear, Integer> priceCol = new TableColumn<>("Daily Price (SEK)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        priceCol.setPrefWidth(150);

        // Add to table
        gearTable.getColumns().addAll(modelCol, typeCol, capacityCol, priceCol);
        gearTable.setItems(gearData);
        gearTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Load vehicle data from the registry into the ObservableList.
    private void loadGearData() {
        List<Gear> gear = Inventory.getInstance().getGearList();
        gearData.addAll(gear);
    }

    // Create a container for Add, Edit, and Remove buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Gear");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> handleAddGear());

        Button btnEdit = new Button("Edit Gear");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditGear());

        Button btnRemove = new Button("Remove Gear");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveGear());

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
// ------------------------------------------------------------------------------------------------------------------
    // --- Action Handlers (Placeholder Logic) ---

    private void handleAddGear() {
        UIUtil.showInfoAlert("Add Member", "Functionality Pending",
                "A dialog/form for adding a new member will be implemented here.");
    }

    private void handleEditGear() {
        Gear selectedGear = gearTable.getSelectionModel().getSelectedItem();
        if (selectedGear != null) {
            UIUtil.showInfoAlert("Edit Vehicle", "Functionality Pending",
                    "A dialog/form for editing vehicle " + selectedGear.getModel() + " will be implemented here.");
        } else {
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required",
                    "Please select a vehicle from the table to edit.");
        }
    }

    private void handleRemoveGear() {
        Gear selectedGear = gearTable.getSelectionModel().getSelectedItem();
        if (selectedGear != null) {
            // In a real app, this would require confirmation and calling the registry service.
            boolean confirmed = true; // Placeholder for a confirmation dialog
            if (confirmed) {
                // Remove from registry and refresh table (Placeholder, actual service call needed)
                gearTable.getItems().remove(selectedGear);
                UIUtil.showInfoAlert("Vehicle Removed", "Success"," " + selectedGear.getModel() + " has been removed.");
                // Note: Actual removal from MemberRegistry object is required in a complete implementation.
            }
        } else {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required",
                    "Please select a member from the table to remove.");
        }
    }
}
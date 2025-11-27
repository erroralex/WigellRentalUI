package com.nilsson.camping.ui.views;

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

public class VehicleView extends VBox {

    private final TableView<Vehicle> vehicleTable = new TableView<>();
    private final ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    public VehicleView() {

        // Apply CSS
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(vehicleTable, Priority.ALWAYS);

        Label title = new Label("Vehicle Management");
        title.getStyleClass().add("content-title");

        // TableView
        initializeTable();
        loadVehicleData();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, vehicleTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        // Make Column
        TableColumn<Vehicle, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        makeCol.setPrefWidth(200);

        // Model Column
        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(250);

        // Year Column
        TableColumn<Vehicle, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(100);

        // Capacity Column
        TableColumn<Vehicle, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.setPrefWidth(100);

        // Type Column
        TableColumn<Vehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);

        // Price Column
        TableColumn<Vehicle, Double> priceCol = new TableColumn<>("Daily Price (SEK)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        priceCol.setPrefWidth(100);

        // Add to table
        vehicleTable.getColumns().addAll(makeCol, modelCol, typeCol, yearCol, capacityCol, priceCol);
        vehicleTable.setItems(vehicleData);
        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Load vehicle data from the registry into the ObservableList.
    private void loadVehicleData() {
        List<Vehicle> vehicles = Inventory.getInstance().getVehicleList();
        vehicleData.addAll(vehicles);
    }

    // Create a container for Add, Edit, and Remove buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Vehicle");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> handleAddVehicle());

        Button btnEdit = new Button("Edit Vehicle");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditVehicle());

        Button btnRemove = new Button("Remove Vehicle");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveVehicle());

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
// ------------------------------------------------------------------------------------------------------------------
    // --- Action Handlers (Placeholder Logic) ---

    private void handleAddVehicle() {
        UIUtil.showInfoAlert("Add Member", "Functionality Pending",
                "A dialog/form for adding a new member will be implemented here.");
    }

    private void handleEditVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            UIUtil.showInfoAlert("Edit Vehicle", "Functionality Pending",
                    "A dialog/form for editing vehicle " + selectedVehicle.getMake() + " will be implemented here.");
        } else {
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required",
                    "Please select a vehicle from the table to edit.");
        }
    }

    private void handleRemoveVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            // In a real app, this would require confirmation and calling the registry service.
            boolean confirmed = true; // Placeholder for a confirmation dialog
            if (confirmed) {
                // Remove from registry and refresh table (Placeholder, actual service call needed)
                vehicleTable.getItems().remove(selectedVehicle);
                UIUtil.showInfoAlert("Vehicle Removed", "Success",
                        selectedVehicle.getMake() + " " + selectedVehicle.getModel() + " has been removed.");
                // Note: Actual removal from MemberRegistry object is required in a complete implementation.
            }
        } else {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required",
                    "Please select a member from the table to remove.");
        }
    }
}
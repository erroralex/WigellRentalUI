package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.service.InventoryService;
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

    private final TableView<RecreationalVehicle> recreationalVehicleTable = new TableView<>();
    private final ObservableList<RecreationalVehicle> recreationalVehicleData = FXCollections.observableArrayList();
    private final InventoryService inventoryService = new InventoryService();

    public VehicleView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(recreationalVehicleTable, Priority.ALWAYS);

        Label title = new Label("Vehicle Management");
        title.getStyleClass().add("content-title");

        // TableView
        initializeTable();
        loadVehicleData();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, recreationalVehicleTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        // Make Column
        TableColumn<RecreationalVehicle, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        makeCol.setPrefWidth(200);

        // Model Column
        TableColumn<RecreationalVehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(250);

        // Year Column
        TableColumn<RecreationalVehicle, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(100);

        // Capacity Column
        TableColumn<RecreationalVehicle, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.setPrefWidth(100);

        // Type Column
        TableColumn<RecreationalVehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);

        // Price Column
        TableColumn<RecreationalVehicle, Double> priceCol = new TableColumn<>("Daily Price (SEK)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        priceCol.setPrefWidth(100);

        // Add to table
        recreationalVehicleTable.getColumns().addAll(makeCol, modelCol, typeCol, yearCol, capacityCol, priceCol);
        recreationalVehicleTable.setItems(recreationalVehicleData);
        recreationalVehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Load vehicle data from the registry into the ObservableList.
    private void loadVehicleData() {
        List<RecreationalVehicle> recreationalVehicles = Inventory.getInstance().getRecreationalVehicleList();
        recreationalVehicleData.addAll(recreationalVehicles);
    }

    private void handleAddRecreationalVehicle() {
        RecreationalVehicle newRecreationalVehicle = inventoryService.handleAddRecreationalVehicle();

        if (newRecreationalVehicle != null) {
            recreationalVehicleData.add(newRecreationalVehicle);
        }
    }

    private void handleEditRecreationalVehicle() {
        RecreationalVehicle selectedRecreationalVehicle = recreationalVehicleTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedRecreationalVehicle == null) {
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required",
                    "Please select a vehicle from the table to edit.");
            return;
        }
        inventoryService.handleEditRecreationalVehicle(selectedRecreationalVehicle);

        // Refresh the TableView.
        refreshTable();
    }

    // Utility method to force the TableView to refresh its content
    private void refreshTable() {
        recreationalVehicleTable.getColumns().get(0).setVisible(false);
        recreationalVehicleTable.getColumns().get(0).setVisible(true);
    }

    // Removing a vehicle, involving both the Service and the View UI update.
    private void handleRemoveRecreationalVehicle() {
        RecreationalVehicle selectedRecreationalVehicle = recreationalVehicleTable.getSelectionModel().getSelectedItem();

        if (selectedRecreationalVehicle == null) {
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required",
                    "Please select a vehicle from the table to remove.");
            return;
        }

        // Confirmation dialog
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Removal",
                "Are you sure?",
                "Do you want to permanently remove " + selectedRecreationalVehicle.getMake() + " " +
                        selectedRecreationalVehicle.getModel() + "?");

        if (confirmed) {
            boolean wasRemovedFromRegistry = inventoryService.handleRemoveRecreationalVehicle(selectedRecreationalVehicle);
            if (wasRemovedFromRegistry) {
                recreationalVehicleData.remove(selectedRecreationalVehicle);
            } else {
                UIUtil.showErrorAlert("Removal Failed", "Operation Error", "The vehicle could not be " +
                        "removed from the registry.");
            }
        }
    }

    // Create a container for Add, Edit, and Remove buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Vehicle");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            RecreationalVehicle newVehicle = inventoryService.handleAddRecreationalVehicle();
            if (newVehicle != null) {
                recreationalVehicleData.add(newVehicle);
            }
        });

        Button btnEdit = new Button("Edit Vehicle");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditRecreationalVehicle());

        Button btnRemove = new Button("Remove Vehicle");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveRecreationalVehicle());

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
}
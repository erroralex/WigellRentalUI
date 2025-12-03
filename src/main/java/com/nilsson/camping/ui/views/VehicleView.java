package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.service.InventoryService;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.ShowAllVehiclesDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class VehicleView extends VBox {

    private final TableView<RecreationalVehicle> recreationalVehicleTable = new TableView<>();
    private final ObservableList<RecreationalVehicle> masterData = FXCollections.observableArrayList();
    private final InventoryService inventoryService = new InventoryService();
    private final TextField searchField = new TextField();
    private FilteredList<RecreationalVehicle> filteredData;

    public VehicleView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(recreationalVehicleTable, Priority.ALWAYS);

        Label title = new Label("Available Vehicles");
        title.getStyleClass().add("content-title");

        // Search Field Setup
        searchField.setPromptText("Search by Make or Model etc...");
        searchField.setMaxWidth(360);

        // TableView
        loadMasterData();
        initializeTable();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, searchField, recreationalVehicleTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        // Make Column
        TableColumn<RecreationalVehicle, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        makeCol.setPrefWidth(150);

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
        capacityCol.setPrefWidth(125);

        // Type Column
        TableColumn<RecreationalVehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);

        // Price Column
        TableColumn<RecreationalVehicle, Double> priceCol = new TableColumn<>("Daily Price (SEK)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));
        priceCol.setPrefWidth(125);

        // Add to table
        recreationalVehicleTable.getColumns().addAll(makeCol, modelCol, typeCol, yearCol, capacityCol, priceCol);
        recreationalVehicleTable.setItems(masterData);

        // Filtering using Streams
        filteredData = new FilteredList<>(masterData, p -> true);

        // Set the filter predicate when the search field text changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(recreationalVehicle -> {
                // If the search field is empty, display all vehicles.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if search string matches
                if (recreationalVehicle.getMake().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (recreationalVehicle.getModel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (recreationalVehicle.getCapacity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (recreationalVehicle.getYear().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (recreationalVehicle.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(recreationalVehicle.getDailyPrice()).contains(lowerCaseFilter)) {
                    return true;
                }
                // No match found.
                return false;
            });
        });

        // Wrap the FilteredList in a SortedList to ensure sorting works with filtering.
        SortedList<RecreationalVehicle> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList's comparator to the TableView's comparator.
        sortedData.comparatorProperty().bind(recreationalVehicleTable.comparatorProperty());

        // Set the sorted data to the TableView.
        recreationalVehicleTable.setItems(sortedData);
    }

    // Load vehicle data from the registry into the ObservableList.
    private void loadMasterData() {
        List<RecreationalVehicle> recreationalVehicles = Inventory.getInstance().getAvailableRecreationalVehicleList();
        masterData.addAll(recreationalVehicles);
    }

    private void handleAddRecreationalVehicle() {
        RecreationalVehicle newRecreationalVehicle = inventoryService.handleAddRecreationalVehicle();

        if (newRecreationalVehicle != null) {
            masterData.add(newRecreationalVehicle);
        }
    }

    private void handleEditRecreationalVehicle() {
        RecreationalVehicle selectedRecreationalVehicle = recreationalVehicleTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedRecreationalVehicle == null) {
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required", "Please select a vehicle from the table to edit.");
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
            UIUtil.showErrorAlert("No Vehicle Selected", "Selection Required", "Please select a vehicle from the table to remove.");
            return;
        }

        // Confirmation dialog
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Removal", "Are you sure?", "Do you want to permanently remove " + selectedRecreationalVehicle.getMake() + " " + selectedRecreationalVehicle.getModel() + "?");

        if (confirmed) {
            boolean wasRemovedFromRegistry = inventoryService.handleRemoveRecreationalVehicle(selectedRecreationalVehicle);
            if (wasRemovedFromRegistry) {
                masterData.remove(selectedRecreationalVehicle);
            } else {
                UIUtil.showErrorAlert("Removal Failed", "Operation Error", "The vehicle could not be " + "removed from the registry.");
            }
        }
    }

    private void handleShowAllVehicles() {
        ShowAllVehiclesDialog dialog = new ShowAllVehiclesDialog();
        dialog.showAndWait();
    }

    // Create a container for Add, Edit, Remove and Show All buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Vehicle");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            RecreationalVehicle newVehicle = inventoryService.handleAddRecreationalVehicle();
            if (newVehicle != null) {
                masterData.add(newVehicle);
            }
        });

        Button btnEdit = new Button("Edit Vehicle");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditRecreationalVehicle());

        Button btnRemove = new Button("Remove Vehicle");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveRecreationalVehicle());

        Button btnShowAll = new Button("Show All Vehicles");
        btnShowAll.getStyleClass().add("action-button");
        btnShowAll.setOnAction(actionEvent -> handleShowAllVehicles());

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove, btnShowAll);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
}
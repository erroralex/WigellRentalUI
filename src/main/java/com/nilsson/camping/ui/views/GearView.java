package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.service.InventoryService;
import com.nilsson.camping.ui.UIUtil;
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

public class GearView extends VBox {

    private final TableView<Gear> gearTable = new TableView<>();
    private final ObservableList<Gear> masterData = FXCollections.observableArrayList();
    private final InventoryService inventoryService = new InventoryService();
    private final TextField searchField = new TextField();
    private FilteredList<Gear> filteredData;

    public GearView() {

        // Apply CSS
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(gearTable, Priority.ALWAYS);

        Label title = new Label("Gear Management");
        title.getStyleClass().add("content-title");

        // Search Field Setup
        searchField.setPromptText("Search by Model or Type etc...");
        searchField.setMaxWidth(315);

        // TableView
        loadMasterData();
        initializeTable();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, searchField, gearTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {

        // Model Column
        TableColumn<Gear, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(200);

        // Type Column
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
        gearTable.setItems(masterData);
        gearTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Filtering using Streams
        // Wrap the master data in a FilteredList.
        filteredData = new FilteredList<>(masterData, p -> true);

        // Set the filter predicate when the search field text changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(gear -> {
                // If the search field is empty, display all gear.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if search string matches
                if (gear.getModel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (gear.getCapacity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (gear.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(gear.getDailyPrice()).contains(lowerCaseFilter)) {
                    return true;
                }
                // No match found.
                return false;
            });
        });

        // Wrap the FilteredList in a SortedList to ensure sorting works with filtering.
        SortedList<Gear> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList's comparator to the TableView's comparator.
        sortedData.comparatorProperty().bind(gearTable.comparatorProperty());

        // Set the sorted data to the TableView.
        gearTable.setItems(sortedData);
    }

    // Load vehicle data from the registry into the ObservableList.
    private void loadMasterData() {
        List<Gear> gearList = Inventory.getInstance().getAvailableGearList();
        masterData.addAll(gearList);
    }

    private void handleAddGear() {
        Gear newGear = inventoryService.handleAddGear();

        if (newGear != null) {
            masterData.add(newGear);
        }
    }

    private void handleEditGear() {
        Gear selectedGear = gearTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedGear == null) {
            UIUtil.showErrorAlert("No Item Selected", "Selection Required",
                    "Please select an item from the table to edit.");
            return;
        }
        inventoryService.handleEditGear(selectedGear);

        // Refresh the TableView.
        refreshTable();
    }

    // Utility method to force the TableView to refresh its content
    private void refreshTable() {
        gearTable.getColumns().get(0).setVisible(false);
        gearTable.getColumns().get(0).setVisible(true);
    }

    private void handleRemoveGear() {
        Gear selectedGear = gearTable.getSelectionModel().getSelectedItem();

        if (selectedGear == null) {
            UIUtil.showErrorAlert("No Item Selected", "Selection Required",
                    "Please select a item from the table to remove.");
            return;
        }

        // Confirmation dialog
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Removal",
                "Are you sure?",
                "Do you want to permanently remove " + selectedGear.getModel() + "?");

        if (confirmed) {
            boolean wasRemovedFromRegistry = inventoryService.handleRemoveGear(selectedGear);

            if (wasRemovedFromRegistry) {
                masterData.remove(selectedGear);
            } else {
                UIUtil.showErrorAlert("Removal Failed", "Operation Error",
                        "The item could not be removed from the registry.");
            }
        }
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
}
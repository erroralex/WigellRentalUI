package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.items.Gear;
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

public class GearView extends VBox {

    private final TableView<Gear> gearTable = new TableView<>();
    private final ObservableList<Gear> gearData = FXCollections.observableArrayList();
    private final InventoryService inventoryService = new InventoryService();

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
        List<Gear> gearList = Inventory.getInstance().getGearList();
        gearData.addAll(gearList);
    }

    private void handleAddGear() {
        Gear newGear = inventoryService.handleAddGear();

        if (newGear != null) {
            gearData.add(newGear);
        }
    }

    private void handleEditGear() {
        Gear selectedGear = gearTable.getSelectionModel().getSelectedItem();

        // 1. Check if an item is selected
        if (selectedGear == null) {
            UIUtil.showErrorAlert("No Item Selected", "Selection Required",
                    "Please select an item from the table to edit.");
            return;
        }

        // 2. Delegate the editing task to the service layer.
        // The service layer handles opening the dialog and updating the Gear object in memory.
        inventoryService.handleEditGear(selectedGear);

        // 3. Refresh the TableView.
        // Since the selectedGear object was modified *in place* by the dialog
        // and service (passed by reference), we just need to tell the table to refresh
        // the display for that item.
        refreshTable();
    }

    /**
     * Utility method to force the TableView to redraw its content,
     * specifically useful after an item in the ObservableList is modified.
     */
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
                gearData.remove(selectedGear);
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
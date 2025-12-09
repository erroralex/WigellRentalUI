package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.registries.RentalRegistry;
import com.nilsson.camping.service.MembershipService;
import com.nilsson.camping.service.ProfitsService;
import com.nilsson.camping.service.RentalService;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.NewRentalDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class RentalView extends VBox {

    private final TableView<Rental> rentalTable = new TableView<>();
    private final ObservableList<Rental> masterData = FXCollections.observableArrayList();

    private final RentalService rentalService = new RentalService();
    private final ProfitsService profitsService = new ProfitsService();
    private final MembershipService membershipService = new MembershipService(); // ADDED: Required for Add Member button

    private final TextField searchField = new TextField();
    private FilteredList<Rental> filteredData;
    private final Runnable externalRefreshAction;

    public RentalView(Runnable refreshAction) {
        this.externalRefreshAction = refreshAction;

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(rentalTable, Priority.ALWAYS);

        Label title = new Label("Rental Management");
        title.getStyleClass().add("content-title");

        // Search Field Setup
        searchField.setPromptText("Search by Member, Item or ID...");
        searchField.setMaxWidth(400);

        // Load data and setup
        loadMasterData();
        initializeTable();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, searchField, rentalTable);
    }

    // Loads rental data from the registry into the ObservableList.
    private void loadMasterData() {
        List<Rental> rentals = RentalRegistry.getInstance().getRentals();
        masterData.setAll(rentals);
    }

    // Replaces all items in the table's master list with the current data from the registry.
    private void refreshData() {
        masterData.setAll(RentalRegistry.getInstance().getRentals());
    }

    // Table setup
    private void initializeTable() {

        // Rental ID
        TableColumn<Rental, String> idCol = new TableColumn<>("Rental ID");
        idCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getRentalId()))
        );

        // Member
        TableColumn<Rental, String> memberCol = new TableColumn<>("Member");
        memberCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        rentalService.getMemberNameFromId(c.getValue().getMemberId())
                )
        );

        // Item
        TableColumn<Rental, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        rentalService.getItemNameFromId(c.getValue().getItemId())
                )
        );

        // Start date
        TableColumn<Rental, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStartDate().toString())
        );

        // Days
        TableColumn<Rental, String> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getRentalDays()))
        );

        // Price
        TableColumn<Rental, String> priceCol = new TableColumn<>("Total (SEK)");
        priceCol.setCellValueFactory(c -> {
            double total = profitsService.calculateRentalRevenue(c.getValue());
            return new SimpleStringProperty(String.format("%.2f", total));
        });

        rentalTable.getColumns().addAll(idCol, memberCol, itemCol, startDateCol, daysCol, priceCol);
        rentalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Filtering using Streams
        filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(rental -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Search by Rental ID
                if (String.valueOf(rental.getRentalId()).contains(lowerCaseFilter)) {
                    return true;
                }

                // Search by Member Name
                String memberName = rentalService.getMemberNameFromId(rental.getMemberId());
                if (memberName != null && memberName.toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Search by Item Name
                String itemName = rentalService.getItemNameFromId(rental.getItemId());
                if (itemName != null && itemName.toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Search by Start Date
                if (rental.getStartDate() != null && rental.getStartDate().toString().contains(lowerCaseFilter)) {
                    return true;
                }

                // Search by Days
                if (String.valueOf(rental.getRentalDays()).contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        // Wrap the FilteredList in a SortedList
        SortedList<Rental> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(rentalTable.comparatorProperty());
        rentalTable.setItems(sortedData);
    }

    // Buttons
    private HBox createButtonBar() {

        // Add Member button
        Button btnAdd = new Button("Add Member");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            membershipService.handleAddMember();
        });

        // New Rental Button
        Button btnNewRental = new Button("New Rental");
        btnNewRental.getStyleClass().add("action-button");
        btnNewRental.setOnAction(e -> openNewRentalDialog());

        // Return Rental button
        Button btnReturnRental = new Button("Return Selected");
        btnReturnRental.getStyleClass().add("action-button");
        btnReturnRental.setOnAction(e -> returnSelectedRental());

        // Add all to box
        HBox box = new HBox(15, btnAdd, btnNewRental, btnReturnRental);
        box.setPadding(new Insets(15, 0, 0, 0));
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    // New Rental
    private void openNewRentalDialog() {
        NewRentalDialog dialog = new NewRentalDialog();
        dialog.showAndWait().ifPresent(result -> {

            Member member = result.getSelectedMember();
            int days = result.getDays();

            // Selected item.
            IRentable item;
            if (result.getSelectedGear() != null) {
                item = result.getSelectedGear();
            } else if (result.getSelectedVehicle() != null) {
                item = result.getSelectedVehicle();
            } else {
                UIUtil.showErrorAlert("New Rental Failed", "No Item Selected", "No item selected for rental.");
                return;
            }

            boolean success = rentalService.handleNewRental(member, item, result.getStartDate(), days);

            if (success) {
                // Refresh all data to include the new rental
                refreshData();

                // Recalculate profits and refresh views
                profitsService.recalculateProfitsFromRentals();
                if (externalRefreshAction != null) {
                    externalRefreshAction.run();
                }
            } else {
                UIUtil.showErrorAlert("New Rental Failed", "Operation Error", "Failed to create new rental.");
            }
        });
    }

    // Return Rental
    private void returnSelectedRental() {
        Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            UIUtil.showErrorAlert("No Rental Selected", "Selection Required", "Please select a rental from the table to return.");
            return;
        }

        // Confirmation pop-up.
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Return", "Are you sure?",
                "Do you want to return the selected item for member " +
                        rentalService.getMemberNameFromId(selectedRental.getMemberId()) + "?");

        if (confirmed) {
            boolean removed = rentalService.handleReturnRental(selectedRental);
            if (removed) {
                // Remove directly from masterData
                masterData.remove(selectedRental);

                // Recalculate profits and refresh views
                profitsService.recalculateProfitsFromRentals();
                if (externalRefreshAction != null) {
                    externalRefreshAction.run();
                }
            } else {
                UIUtil.showErrorAlert("Return Failed", "Operation Error", "The rental could not be removed from the registry.");
            }
        }
    }
}
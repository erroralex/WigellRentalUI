package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.registries.RentalRegistry;
import com.nilsson.camping.service.MembershipService;
import com.nilsson.camping.service.RentalService;
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

public class RentalView extends VBox {

    private final TableView<Rental> rentalTable = new TableView<>();
    private final ObservableList<Member> masterMemberData = FXCollections.observableArrayList();
    private final ObservableList<Rental> masterData = FXCollections.observableArrayList();
    private final RentalService rentalService = new RentalService();
    private final MembershipService membershipService = new MembershipService();
    private final TextField searchField = new TextField();
    private FilteredList<Rental> filteredData;

    public RentalView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(rentalTable, Priority.ALWAYS);

        Label title = new Label("Rental Management");
        title.getStyleClass().add("content-title");

        // Search Field Setup
        searchField.setPromptText("Search by Member, Item or Type...");
        searchField.setMaxWidth(400);

        // TableView
        loadMasterData();
        initializeTable();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, searchField, rentalTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {

        // Rental ID Column
        TableColumn<Rental, Integer> idCol = new TableColumn<>("Rental #");
        idCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        idCol.setPrefWidth(100);

        // Member Column
        TableColumn<Rental, String> memberCol = new TableColumn<>("Member");
        memberCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        memberCol.setPrefWidth(225);

        // Item Column
        TableColumn<Rental, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemCol.setPrefWidth(220);

        // Type Column
        TableColumn<Rental, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        typeCol.setPrefWidth(120);

        // Start Date Column
        TableColumn<Rental, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateCol.setPrefWidth(140);

        // Days Column
        TableColumn<Rental, Integer> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("rentalDays"));
        daysCol.setPrefWidth(101);

        rentalTable.getColumns().addAll(idCol, memberCol, itemCol, typeCol, startDateCol, daysCol);

        rentalTable.setItems(masterData);

        // Filtering using Streams
        filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(rental -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(rental.getRentalId()).contains(lowerCaseFilter)) {
                    return true;
                } else if (rental.getMemberName() != null && rental.getMemberName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rental.getItemName() != null && rental.getItemName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rental.getItemType() != null && rental.getItemType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rental.getStartDate() != null && rental.getStartDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(rental.getRentalDays()).contains(lowerCaseFilter)) {
                    return true;
                }

                // No match found
                return false;
            });
        });

        // Wrap the FilteredList in a SortedList to ensure sorting works with filtering.
        SortedList<Rental> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList's comparator to the TableView's comparator.
        sortedData.comparatorProperty().bind(rentalTable.comparatorProperty());

        // Set the sorted data to the TableView.
        rentalTable.setItems(sortedData);
    }

    // Loads rental data from the registry into the ObservableList.
    private void loadMasterData() {
        List<Rental> rentals = RentalRegistry.getInstance().getRentals();
        masterData.addAll(rentals);
    }

    // Utility method to force the TableView to refresh its content
    private void refreshTable() {
        rentalTable.getColumns().get(0).setVisible(false);
        rentalTable.getColumns().get(0).setVisible(true);
    }

    // Handle returning a rental
    private void handleReturnRental() {
        Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            UIUtil.showErrorAlert("No Rental Selected", "Selection Required", "Please select a rental from the table to return.");
            return;
        }

        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Return", "Are you sure?", "Do you want to return the selected item for member " + selectedRental.getMemberName() + "?");

        if (confirmed) {
            boolean removed = rentalService.handleReturnRental(selectedRental);
            if (removed) {
                masterData.remove(selectedRental);
            } else {
                UIUtil.showErrorAlert("Return Failed", "Operation Error", "The rental could not be removed from the registry.");
            }
        }
    }

    // Create a container for Add Member, New Rental and Return Item buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Member");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            Member newMember = membershipService.handleAddMember();

            // Check if the member was successfully created and added
            if (newMember != null) {
                masterMemberData.add(newMember);
            }
        });

        Button btnNewRental = new Button("New Rental");
        btnNewRental.getStyleClass().add("action-button");
        btnNewRental.setOnAction(actionEvent -> {
            Rental newRental = rentalService.handleNewRental();
            if (newRental != null) {
                masterData.add(newRental);
            }
        });

        Button btnReturn = new Button("Return Item");
        btnReturn.getStyleClass().add("action-button");
        btnReturn.setOnAction(actionEvent -> handleReturnRental());

        HBox buttonBar = new HBox(10, btnAdd, btnNewRental, btnReturn);
        buttonBar.setAlignment(Pos.CENTER_LEFT);

        return buttonBar;
    }
}
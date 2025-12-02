package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.service.MemberService;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.HistoryDialog;
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

public class MemberView extends VBox {

    private final TableView<Member> memberTable = new TableView<>();
    private final ObservableList<Member> masterMemberData = FXCollections.observableArrayList();
    private final MemberService memberService = new MemberService();
    private final TextField searchField = new TextField();
    private FilteredList<Member> filteredData;

    public MemberView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(memberTable, Priority.ALWAYS);

        Label title = new Label("Member Management");
        title.getStyleClass().add("content-title");

        // Search Field Setup
        searchField.setPromptText("Search by Name, Level or ID...");
        searchField.setMaxWidth(385);

        // TableView
        loadMasterData();
        initializeTable();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, searchField, memberTable);
    }

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        // ID Column
        TableColumn<Member, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        // First Name Column
        TableColumn<Member, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(200);

        // Last Name Column
        TableColumn<Member, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(200);

        // Membership Level Column
        TableColumn<Member, String> membershipCol = new TableColumn<>("Membership Level");
        membershipCol.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));
        membershipCol.setPrefWidth(150);

        // Add to table
        memberTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, membershipCol);
        memberTable.setItems(masterMemberData);
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Filtering using Streams
        filteredData = new FilteredList<>(masterMemberData, p -> true);

        // Set the filter predicate when the search field text changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(member -> {
                // If the search field is empty, display all members.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if search string matches
                if (member.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (member.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (member.getMembershipLevel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(member.getId()).contains(lowerCaseFilter)) {
                    return true;
                }
                // No match found.
                return false;
            });
        });

        // Wrap the FilteredList in a SortedList to ensure sorting works with filtering.
        SortedList<Member> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList's comparator to the TableView's comparator.
        sortedData.comparatorProperty().bind(memberTable.comparatorProperty());

        // Set the sorted data to the TableView.
        memberTable.setItems(sortedData);
    }

    // Loads member data from the registry into the ObservableList.
    private void loadMasterData() {
        List<Member> members = MemberRegistry.getInstance().getMembers();
        masterMemberData.addAll(members);
    }

    private void handleEditMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedMember == null) {
            UIUtil.showErrorAlert("No Item Selected", "Selection Required", "Please select an item from the table to edit.");
            return;
        }
        memberService.handleEditMember(selectedMember);

        // Refresh the TableView.
        refreshTable();
    }

    // Utility method to force the TableView to refresh its content
    private void refreshTable() {
        memberTable.getColumns().get(0).setVisible(false);
        memberTable.getColumns().get(0).setVisible(true);
    }

    // Removing a member, involving both the Service and the View (UI update).
    private void handleRemoveMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required", "Please select a member from the table to remove.");
            return;
        }

        // Confirmation dialog
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Removal", "Are you sure?", "Do you want to permanently remove " + selectedMember.getFirstName() + "?");

        if (confirmed) {
            boolean wasRemovedFromRegistry = memberService.removeMemberFromRegistry(selectedMember);
            if (wasRemovedFromRegistry) {
                masterMemberData.remove(selectedMember);
            } else {
                UIUtil.showErrorAlert("Removal Failed", "Operation Error", "The member could not be removed from the registry.");
            }
        }
    }

    private void handleShowHistory() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        // 1. Check if a member is selected
        if (selectedMember == null) {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required", "Please select a member from the table to view their history.");
            return;
        }

        // 2. Instantiate and show the dedicated HistoryDialog
        HistoryDialog historyDialog = new HistoryDialog(selectedMember);
        historyDialog.showAndWait(); // Use showAndWait() to block the main view until closed
    }

    // Container for Add, Edit, and Remove buttons.
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Member");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            Member newMember = memberService.handleAddMember();

            // Check if the member was successfully created and added
            if (newMember != null) {
                masterMemberData.add(newMember);
            }
        });

        Button btnEdit = new Button("Edit Member");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditMember());

        Button btnRemove = new Button("Remove Member");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveMember());

        Button btnHistory = new Button("Member History");
        btnHistory.getStyleClass().add("action-button");
        btnHistory.setOnAction(actionEvent -> handleShowHistory());

        // Add to container
        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove, btnHistory);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
}
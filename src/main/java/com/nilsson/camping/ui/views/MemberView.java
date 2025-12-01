package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.service.MembershipService;
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

public class MemberView extends VBox {

    private final TableView<Member> memberTable = new TableView<>();
    private final ObservableList<Member> memberData = FXCollections.observableArrayList();
    private final MembershipService membershipService = new MembershipService();

    public MemberView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(memberTable, Priority.ALWAYS);

        Label title = new Label("Member Management");
        title.getStyleClass().add("content-title");

        // TableView
        initializeTable();
        loadMemberData();

        HBox buttonBar = createButtonBar();

        // Add all sections to the main VBox
        this.getChildren().addAll(title, buttonBar, memberTable);
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
        memberTable.setItems(memberData);
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Loads member data from the registry into the ObservableList.
    private void loadMemberData() {
        List<Member> members = MemberRegistry.getInstance().getMembers();
        memberData.addAll(members);
    }

    private void handleEditMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        // 1. Check if an item is selected
        if (selectedMember == null) {
            UIUtil.showErrorAlert("No Item Selected", "Selection Required",
                    "Please select an item from the table to edit.");
            return;
        }

        // 2. Delegate the editing task to the service layer.
        // The service layer handles opening the dialog and updating the Gear object in memory.
        membershipService.handleEditMember(selectedMember);

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
        memberTable.getColumns().get(0).setVisible(false);
        memberTable.getColumns().get(0).setVisible(true);
    }

    //Removing a member, involving both the Service and the View (UI update).
    private void handleRemoveMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required",
                    "Please select a member from the table to remove.");
            return;
        }

        // Confirmation dialog
        boolean confirmed = UIUtil.showConfirmationAlert("Confirm Removal",
                "Are you sure?",
                "Do you want to permanently remove " + selectedMember.getFirstName() + "?");

        if (confirmed) {
            boolean wasRemovedFromRegistry = membershipService.removeMemberFromRegistry(selectedMember);
            if (wasRemovedFromRegistry) {
                memberData.remove(selectedMember);
            } else {
                UIUtil.showErrorAlert("Removal Failed", "Operation Error", "The member could not be removed from the registry.");
            }
        }
    }


    // Container for Add, Edit, and Remove buttons.
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Member");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> {
            Member newMember = membershipService.handleAddMember(); // Call the service

            // Check if the member was successfully created and added (not null)
            if (newMember != null) {
                // This is the key: adding to the ObservableList immediately updates the TableView.
                memberData.add(newMember);
            }
        });

        Button btnEdit = new Button("Edit Member");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent ->
                membershipService.handleEditMember(memberTable.getSelectionModel().getSelectedItem()));

        Button btnRemove = new Button("Remove Member");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveMember());

        // Add to container
        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
}
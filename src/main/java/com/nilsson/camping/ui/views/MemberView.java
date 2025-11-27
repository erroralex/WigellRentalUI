package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
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

    public MemberView() {

        // Apply CSS
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
        idCol.setPrefWidth(200);

        // Last Name Column
        TableColumn<Member, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(200);

        // Membership Level Column
        TableColumn<Member, String> membershipCol = new TableColumn<>("Membership Level");
        membershipCol.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));
        idCol.setPrefWidth(150);

        // Add to table
        memberTable.getColumns().addAll(idCol,firstNameCol,lastNameCol, membershipCol);
        memberTable.setItems(memberData);
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Load member data from the registry into the ObservableList.
    private void loadMemberData() {
        List<Member> members = MemberRegistry.getInstance().getMembers();
        memberData.addAll(members);
    }

    // Create a container for Add, Edit, and Remove buttons
    private HBox createButtonBar() {

        Button btnAdd = new Button("Add Member");
        btnAdd.getStyleClass().add("action-button");
        btnAdd.setOnAction(actionEvent -> handleAddMember());

        Button btnEdit = new Button("Edit Member");
        btnEdit.getStyleClass().add("action-button");
        btnEdit.setOnAction(actionEvent -> handleEditMember());

        Button btnRemove = new Button("Remove Member");
        btnRemove.getStyleClass().add("action-button");
        btnRemove.setOnAction(actionEvent -> handleRemoveMember());

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnRemove);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        return buttonBar;
    }
// ------------------------------------------------------------------------------------------------------------------
    // --- Action Handlers (Placeholder Logic) ---

    private void handleAddMember() {
        UIUtil.showInfoAlert("Add Member", "Functionality Pending",
                "A dialog/form for adding a new member will be implemented here.");
    }

    private void handleEditMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            UIUtil.showInfoAlert("Edit Member", "Functionality Pending",
                    "A dialog/form for editing member " + selectedMember.getFirstName() + " will be implemented here.");
        } else {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required",
                    "Please select a member from the table to edit.");
        }
    }

    private void handleRemoveMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            // In a real app, this would require confirmation and calling the registry service.
            boolean confirmed = true; // Placeholder for a confirmation dialog
            if (confirmed) {
                // Remove from registry and refresh table (Placeholder, actual service call needed)
                memberTable.getItems().remove(selectedMember);
                UIUtil.showInfoAlert("Member Removed", "Success",
                        selectedMember.getFirstName() + " " + selectedMember.getLastName() + " has been removed.");
                // Note: Actual removal from MemberRegistry object is required in a complete implementation.
            }
        } else {
            UIUtil.showErrorAlert("No Member Selected", "Selection Required",
                    "Please select a member from the table to remove.");
        }
    }
}
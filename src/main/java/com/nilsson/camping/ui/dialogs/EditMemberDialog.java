package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.ui.UIUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class EditMemberDialog extends Dialog<Member> {

    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final ComboBox<String> levelBox = new ComboBox<>();

    private static final String DEFAULT_LEVEL = "Standard";
    private static final String PREMIUM_LEVEL = "Premium";
    private static final String STUDENT_LEVEL = "Student";

    private final Member memberToEdit;

    public EditMemberDialog(Member memberToEdit) {

        this.memberToEdit = memberToEdit;

        setTitle("Edit Member");
        setHeaderText("Enter the details for " + memberToEdit.getFirstName() + " " + memberToEdit.getLastName());

        // Apply theme and mouse-drag
        this.setOnShowing(dialogEvent -> {
            UIUtil.applyDialogSetup(this);
        });

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().clear();
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Form Layout
        GridPane grid = createGridPane();;

        // ComboBox
        levelBox.getItems().addAll(DEFAULT_LEVEL, PREMIUM_LEVEL, STUDENT_LEVEL);
        levelBox.setValue(DEFAULT_LEVEL);
        levelBox.setMaxWidth(Double.MAX_VALUE);

        // Grid layout
        grid.add(new Label("First Name"), 0, 0);
        grid.add(firstNameField, 1, 0);
        firstNameField.setText(memberToEdit.getFirstName());
        firstNameField.setPromptText("(e.g., John)");

        grid.add(new Label("Last Name"), 0, 1);
        grid.add(lastNameField, 1, 1);
        lastNameField.setText(memberToEdit.getLastName());

        grid.add(new Label("Membership Level"), 0, 2);
        grid.add(levelBox, 1, 2);

        getDialogPane().setContent(grid);

        // Focus
        Platform.runLater(firstNameField::requestFocus);

        // Enable/disable button
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(
                firstNameField.textProperty().isEmpty()
                        .or(lastNameField.textProperty().isEmpty())
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                memberToEdit.setFirstName(firstNameField.getText().trim());
                memberToEdit.setLastName(lastNameField.getText().trim());
                memberToEdit.setMembershipLevel(levelBox.getValue());
                return memberToEdit;
            }
            // If Cancel is clicked
            return null;
        });
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(20,20,10,10));
        return grid;
    }
}

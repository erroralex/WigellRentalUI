package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.NewRentalResult;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.ui.UIUtil;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;

public class NewRentalDialog extends Dialog<NewRentalResult> {

    private final ComboBox<Member> memberBox = new ComboBox<>();
    private final ComboBox<String> itemTypeBox = new ComboBox<>();
    private final ComboBox<Gear> gearBox = new ComboBox<>();
    private final ComboBox<RecreationalVehicle> vehicleBox = new ComboBox<>();
    private final DatePicker startDatePicker = new DatePicker(LocalDate.now());
    private final TextField daysField = new TextField();

    private static final String TYPE_GEAR = "Gear";
    private static final String TYPE_VEHICLE = "Vehicle";

    public NewRentalDialog() {

        setTitle("New Rental");
        setHeaderText("Choose member, item and rental period.");

        // Theme / drag support
        this.setOnShowing(event -> UIUtil.applyDialogSetup(this));

        ButtonType createButtonType = new ButtonType("Create Rental", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = createGridPane();

        // Member combo
        List<Member> members = MemberRegistry.getInstance().getMembers();
        memberBox.getItems().addAll(members);
        memberBox.setMaxWidth(Double.MAX_VALUE);
        memberBox.setPromptText("Select member");

        // Show member nicely
        memberBox.setCellFactory(cb -> new ListCell<Member>() {
            @Override
            protected void updateItem(Member item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName() + " (ID: " + item.getId() + ")");
                }
            }
        });
        memberBox.setButtonCell(memberBox.getCellFactory().call(null));

        // Item type
        itemTypeBox.getItems().addAll(TYPE_GEAR, TYPE_VEHICLE);
        itemTypeBox.setValue(TYPE_GEAR);
        itemTypeBox.setMaxWidth(Double.MAX_VALUE);

        // Item combos (start as gear)
        loadAvailableGear();
        loadAvailableVehicles();

        gearBox.setMaxWidth(Double.MAX_VALUE);
        vehicleBox.setMaxWidth(Double.MAX_VALUE);

        // How to display items
        gearBox.setCellFactory(cb -> new ListCell<Gear>() {
            @Override
            protected void updateItem(Gear item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getModel() + " (" + item.getType() + ")");
            }
        });
        gearBox.setButtonCell(gearBox.getCellFactory().call(null));

        vehicleBox.setCellFactory(cb -> new ListCell<RecreationalVehicle>() {
            @Override
            protected void updateItem(RecreationalVehicle item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getMake() + " " + item.getModel() + " (" + item.getType() + ")");
            }
        });
        vehicleBox.setButtonCell(vehicleBox.getCellFactory().call(null));

        // Days
        daysField.setPromptText("(e.g., 3)");

        // Layout
        grid.add(new Label("Member"), 0, 0);
        grid.add(memberBox, 1, 0);

        grid.add(new Label("Item Type"), 0, 1);
        grid.add(itemTypeBox, 1, 1);

        grid.add(new Label("Gear Item"), 0, 2);
        grid.add(gearBox, 1, 2);

        grid.add(new Label("Vehicle Item"), 0, 3);
        grid.add(vehicleBox, 1, 3);

        grid.add(new Label("Start Date"), 0, 4);
        grid.add(startDatePicker, 1, 4);

        grid.add(new Label("Number of Days"), 0, 5);
        grid.add(daysField, 1, 5);

        getDialogPane().setContent(grid);

        // Toggle which item combo is active
        updateItemComboVisibility();
        itemTypeBox.valueProperty().addListener((obs, oldV, newV) -> updateItemComboVisibility());

        // Focus
        Platform.runLater(memberBox::requestFocus);

        // Enable/disable button
        Button createButton = (Button) getDialogPane().lookupButton(createButtonType);
        createButton.disableProperty().bind(
                memberBox.valueProperty().isNull()
                        .or(startDatePicker.valueProperty().isNull())
                        .or(daysField.textProperty().isEmpty())
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                Member member = memberBox.getValue();
                String type = itemTypeBox.getValue();

                Gear gear = null;
                RecreationalVehicle vehicle = null;

                if (TYPE_GEAR.equals(type)) {
                    gear = gearBox.getValue();
                    if (gear == null) {
                        UIUtil.showErrorAlert("Missing Item", "No Gear Selected",
                                "Please select a gear item to rent.");
                        return null;
                    }
                } else {
                    vehicle = vehicleBox.getValue();
                    if (vehicle == null) {
                        UIUtil.showErrorAlert("Missing Item", "No Vehicle Selected",
                                "Please select a vehicle to rent.");
                        return null;
                    }
                }

                int days;
                try {
                    days = Integer.parseInt(daysField.getText().trim());
                    if (days <= 0) {
                        throw new NumberFormatException("days must be > 0");
                    }
                } catch (NumberFormatException ex) {
                    UIUtil.showErrorAlert("Invalid Days", "Input Error",
                            "Please enter a positive whole number for days.");
                    return null;
                }

                return new NewRentalResult(
                        member,
                        gear,
                        vehicle,
                        startDatePicker.getValue(),
                        days
                );
            }
            return null;
        });
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 20, 10, 10));
        return grid;
    }

    private void updateItemComboVisibility() {
        boolean gearSelected = TYPE_GEAR.equals(itemTypeBox.getValue());
        gearBox.setDisable(!gearSelected);
        vehicleBox.setDisable(gearSelected);
        if (gearSelected) {
            vehicleBox.setValue(null);
        } else {
            gearBox.setValue(null);
        }
    }

    private void loadAvailableGear() {
        gearBox.getItems().setAll(
                Inventory.getInstance().getAvailableGearList()
        );
    }

    private void loadAvailableVehicles() {
        vehicleBox.getItems().setAll(
                Inventory.getInstance().getAvailableRecreationalVehicleList()
        );
    }
}

package com.nilsson.camping.service;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.NewRentalResult;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.model.registries.RentalRegistry;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.NewRentalDialog;

import java.util.Optional;

public class RentalService {

    public Rental handleNewRental() {
        // TODO: open NewRentalDialog, build Rental from selected member/item and duration
        // 1. Let user pick member + item via dialog (pseudo)
        NewRentalDialog dialog = new NewRentalDialog();
        Optional<NewRentalResult> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return null;
        }

        NewRentalResult data = result.get();

// 2. Mark item as rented
        if (data.getSelectedVehicle() != null) {
            RecreationalVehicle rv = data.getSelectedVehicle();
            rv.setRented(true);
            DataHandler.saveRecreationalVehicle(
                    Inventory.getInstance().getRecreationalVehicleList());
        } else if (data.getSelectedGear() != null) {
            Gear gear = data.getSelectedGear();
            gear.setRented(true);
            DataHandler.saveGear(Inventory.getInstance().getGearList());
        }

        // 3. Create Rental and persist
        // Let RentalRegistry assign the ID
        int newId = RentalRegistry.getInstance().getNextId(); // add this method, see below

        Rental newRental = new Rental(
                newId,
                data.getSelectedMember().getFirstName() + " " +
                        data.getSelectedMember().getLastName() +
                        " (ID: " + data.getSelectedMember().getId() + ")",
                data.getSelectedVehicle() != null
                        ? data.getSelectedVehicle().getModel()
                        : data.getSelectedGear().getModel(),
                data.getSelectedVehicle() != null ? "Vehicle" : "Gear",
                data.getStartDate().toString(),
                data.getDays()
        );

        RentalRegistry.getInstance().addRental(newRental);

        UIUtil.showInfoAlert("Rental Created", "Success",
                "The item has been successfully rented out.");

        return newRental;
    }

    public boolean handleReturnRental(Rental selectedRental) {
        // TODO: optional: update history, free item, etc.
        if (selectedRental == null) {
            return false;
        }
        // Try to match by name and type
        Inventory inventory = Inventory.getInstance();

        if ("Vehicle".equalsIgnoreCase(selectedRental.getItemType())) {
            inventory.getRecreationalVehicleList().stream()
                    .filter(rv -> rv.getModel().equals(selectedRental.getItemName()))
                    .findFirst()
                    .ifPresent(rv -> rv.setRented(false));
            DataHandler.saveRecreationalVehicle(inventory.getRecreationalVehicleList());
        } else if ("Gear".equalsIgnoreCase(selectedRental.getItemType())) {
            inventory.getGearList().stream()
                    .filter(g -> g.getModel().equals(selectedRental.getItemName()))
                    .findFirst()
                    .ifPresent(g -> g.setRented(false));
            DataHandler.saveGear(inventory.getGearList());
        }

// Remove rental from registry + JSON
        boolean removed = RentalRegistry.getInstance().removeRental(selectedRental);

        if (removed) {
            UIUtil.showInfoAlert("Item Returned", "Success",
                    "The item has been successfully returned.");
        }

        return removed;
    }
}

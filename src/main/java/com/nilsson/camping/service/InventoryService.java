package com.nilsson.camping.service;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.EditGearDialog;
import com.nilsson.camping.ui.dialogs.EditVehicleDialog;
import com.nilsson.camping.ui.dialogs.AddVehicleDialog;

import java.util.Optional;

public class InventoryService {

    // ──────────────────────────────────────────────────────
    // Vehicle Operations
    // ──────────────────────────────────────────────────────
    public RecreationalVehicle handleAddRecreationalVehicle() {
        // Display the input form and collect data
        AddVehicleDialog dialog = new AddVehicleDialog();
        Optional<RecreationalVehicle> result = dialog.showAndWait();

        if (result.isPresent()) {
            RecreationalVehicle newVehicleData = result.get();

            // Create a new Vehicle object
            Inventory inventory = Inventory.getInstance();

            // Add to registry
            inventory.addRecreationalVehicle(newVehicleData);

            // Show success confirmation
            UIUtil.showInfoAlert("Vehicle Added", "Success",
                    newVehicleData.getMake() + " " + newVehicleData.getModel() + " has been successfully added.");
            return newVehicleData;
        }
        // Cancel was clicked
        return null;
    }


    // Accept the Vehicle object from the View
    public void handleEditRecreationalVehicle(RecreationalVehicle selectedRecreationalVehicle) {
        if (selectedRecreationalVehicle == null) {
            UIUtil.showErrorAlert("Edit Error", "No Vehicle Selected", "Please select a vehicle to edit.");
            return;
        }

        EditVehicleDialog dialog = new EditVehicleDialog(selectedRecreationalVehicle);
        Optional<RecreationalVehicle> result = dialog.showAndWait();

        if (result.isPresent()) {
            RecreationalVehicle updatedRecreationalVehicle = result.get();

            DataHandler.saveRecreationalVehicle(Inventory.getInstance().getRecreationalVehicleList());

            UIUtil.showInfoAlert("Vehicle Updated", "Success",
                    updatedRecreationalVehicle.getModel() + " " + updatedRecreationalVehicle.getMake() + " has been successfully updated.");
        }
    }

    public boolean handleRemoveRecreationalVehicle(RecreationalVehicle selectedRecreationalVehicle) {
        if (selectedRecreationalVehicle == null) {
            return false;
        }

        // This method removes the item from the in-memory list AND calls DataHandler.saveRecreationalVehicle().
        Inventory.getInstance().removeRecreationalVehicle(selectedRecreationalVehicle);

        // The UIUtil alert should ideally be called based on the result of the removal.
        UIUtil.showInfoAlert("Recreational Vehicle Removed", "Success", selectedRecreationalVehicle.getMake() + " " + selectedRecreationalVehicle.getModel() + " has been removed.");
        return true;
    }

    // ──────────────────────────────────────────────────────
    // Gear Operations
    // ──────────────────────────────────────────────────────

    public Gear handleAddGear() {
        // Display the input form and collect data
        EditGearDialog dialog = new EditGearDialog();
        Optional<Gear> result = dialog.showAndWait();

        if (result.isPresent()) {
            Gear newGearData = result.get();

            // Create a new Vehicle object
            Inventory inventory = Inventory.getInstance();

            // Add to registry
            inventory.addGear(newGearData);

            // Show success confirmation
            UIUtil.showInfoAlert("Gear Added", "Success",
                    newGearData.getModel() + " has been successfully added.");
            return newGearData;
        }
        // Cancel was clicked
        return null;
    }

    public void handleEditGear(Gear selectedGear) {
        if (selectedGear == null) {
            UIUtil.showErrorAlert("Edit Error", "No Gear Selected", "Please select a gear item to edit.");
            return;
        }

        EditGearDialog dialog = new EditGearDialog(selectedGear);
        Optional<Gear> result = dialog.showAndWait();

        if (result.isPresent()) {
            Gear updatedGear = result.get();

            DataHandler.saveGear(Inventory.getInstance().getGearList());

            UIUtil.showInfoAlert("Gear Updated", "Success",
                    updatedGear.getModel() + " has been successfully updated.");
        }
    }

    public boolean handleRemoveGear(Gear selectedGear) {
        if (selectedGear == null) {
            return false;
        }

        // Call the Inventory registry's removal method.
        boolean wasRemoved = Inventory.getInstance().removeGear(selectedGear);

        if (wasRemoved) {
            // Show success confirmation only if the removal was successful
            UIUtil.showInfoAlert("Gear Removed", "Success", selectedGear.getModel() + " has been successfully removed.");
        } else {
            // Error handling if registry failed to remove
            UIUtil.showErrorAlert("Removal Error", "Registry Mismatch", "Could not find the selected gear in the inventory registry.");
        }
        return wasRemoved;
    }
}
package com.nilsson.camping.service;

import com.nilsson.camping.app.LanguageManager;
import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.AddGearDialog;
import com.nilsson.camping.ui.dialogs.EditGearDialog;
import com.nilsson.camping.ui.dialogs.EditVehicleDialog;
import com.nilsson.camping.ui.dialogs.AddVehicleDialog;
import java.util.List;
import java.util.Optional;

public class InventoryService {

    // ──────────────────────────────────────────────────────
    //                  Vehicle Operations
    // ──────────────────────────────────────────────────────
    public RecreationalVehicle handleAddRecreationalVehicle() {

        // Display the input form and collect data
        AddVehicleDialog dialog = new AddVehicleDialog();
        Optional<RecreationalVehicle> result = dialog.showAndWait();

        if (result.isPresent()) {

            RecreationalVehicle newVehicleData = result.get();
            Inventory inventory = Inventory.getInstance();

            // Add to registry
            inventory.addRecreationalVehicle(newVehicleData);

            // Show success confirmation
            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.vehicleAdded"),
                    LanguageManager.getInstance().getString("msg.success"),
                    newVehicleData.getMake() + " " + newVehicleData.getModel() +
                            LanguageManager.getInstance().getString("msg.addedSuccess"));
            return newVehicleData;
        }
        // Cancel was clicked
        return null;
    }

    // Accept the Vehicle object from the View
    public void handleEditRecreationalVehicle(RecreationalVehicle selectedRecreationalVehicle) {
        if (selectedRecreationalVehicle == null) {
            UIUtil.showErrorAlert(
                    LanguageManager.getInstance().getString("error.editError"),
                    LanguageManager.getInstance().getString("error.noItemSelected"),
                    LanguageManager.getInstance().getString("error.pleaseSelectEditItem"));
            return;
        }

        EditVehicleDialog dialog = new EditVehicleDialog(selectedRecreationalVehicle);
        Optional<RecreationalVehicle> result = dialog.showAndWait();

        if (result.isPresent()) {
            RecreationalVehicle updatedRecreationalVehicle = result.get();

            DataHandler.saveRecreationalVehicle(Inventory.getInstance().getRecreationalVehicleList());

            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.vehicleUpdated"),
                    LanguageManager.getInstance().getString("msg.success"),
                    updatedRecreationalVehicle.getModel() + " " + updatedRecreationalVehicle.getMake() +
                            LanguageManager.getInstance().getString("msg.updateSuccess"));
        }
    }

    public boolean handleRemoveRecreationalVehicle(RecreationalVehicle selectedRecreationalVehicle) {
        if (selectedRecreationalVehicle == null) {
            return false;
        }

        Inventory.getInstance().removeRecreationalVehicle(selectedRecreationalVehicle);
        UIUtil.showInfoAlert(
                LanguageManager.getInstance().getString("msg.vehicleRemoved"),
                LanguageManager.getInstance().getString("msg.success"),
                selectedRecreationalVehicle.getMake()
                + " " + selectedRecreationalVehicle.getModel() +
                        LanguageManager.getInstance().getString("msg.removedSuccess"));
        return true;
    }

    // ──────────────────────────────────────────────────────
    //                  Gear Operations
    // ──────────────────────────────────────────────────────

    public Gear handleAddGear() {

        // Display the input form and collect data
        AddGearDialog dialog = new AddGearDialog();
        Optional<Gear> result = dialog.showAndWait();

        if (result.isPresent()) {

            Gear newGearData = result.get();
            Inventory inventory = Inventory.getInstance();

            // Add to registry
            inventory.addGear(newGearData);

            // Show success confirmation
            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.gearAdded"),
                    LanguageManager.getInstance().getString("msg.success"),
                    newGearData.getModel() +
                            LanguageManager.getInstance().getString("msg.addedSuccess"));
            return newGearData;
        }
        // Cancel was clicked
        return null;
    }

    public void handleEditGear(Gear selectedGear) {
        if (selectedGear == null) {
            UIUtil.showErrorAlert(
                    LanguageManager.getInstance().getString("error.editError"),
                    LanguageManager.getInstance().getString("error.noItemSelected"),
                    LanguageManager.getInstance().getString("error.pleaseSelectEditItem"));
            return;
        }

        EditGearDialog dialog = new EditGearDialog(selectedGear);
        Optional<Gear> result = dialog.showAndWait();

        if (result.isPresent()) {
            Gear updatedGear = result.get();

            DataHandler.saveGear(Inventory.getInstance().getGearList());

            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.gearAdded"),
                    LanguageManager.getInstance().getString("msg.success"),
                    updatedGear.getModel() +
                            LanguageManager.getInstance().getString("msg.updateSuccess"));
        }
    }

    public boolean handleRemoveGear(Gear selectedGear) {
        if (selectedGear == null) {
            return false;
        }

        boolean wasRemoved = Inventory.getInstance().removeGear(selectedGear);

        if (wasRemoved) {
            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.gearRemoved"),
                    LanguageManager.getInstance().getString("msg.success"),
                    selectedGear.getModel() +
                            LanguageManager.getInstance().getString("msg.removedSuccess"));
        } else {
            UIUtil.showErrorAlert(
                    LanguageManager.getInstance().getString("error.removalFailed"),
                    LanguageManager.getInstance().getString("error.registryMismatch"),
                    LanguageManager.getInstance().getString("error.couldNotFindGear"));
        }
        return wasRemoved;
    }

    // ──────────────────────────────────────────────────────
    //                  Data Persistence
    // ──────────────────────────────────────────────────────

    public IRentable findRentableItem(String itemType, String itemName) {

        Inventory inventory = Inventory.getInstance();

        // Search Recreational Vehicles
        List<RecreationalVehicle> vehicles = inventory.getAvailableRecreationalVehicleList();

        for (RecreationalVehicle vehicle : vehicles) {
            // Check if both type and name match
            if (vehicle.getItemType().equals(itemType) && vehicle.getItemName().equals(itemName)) {
                return vehicle;
            }
        }

        // Search Gear Items
        List<Gear> gearItems = inventory.getAvailableGearList();

        for (Gear gear : gearItems) {
            // Check if both type and name match
            if (gear.getItemType().equals(itemType) && gear.getItemName().equals(itemName)) {
                return gear;
            }
        }

        // Item not found
        return null;
    }

    // Saves both the Recreational Vehicles list and the Gear list by calling the DataHandler methods.
    public void saveAllInventory() {

        Inventory inventory = Inventory.getInstance();

        // Save Vehicles
        DataHandler.saveRecreationalVehicle(inventory.getAvailableRecreationalVehicleList());

        // Save Gear
        DataHandler.saveGear(inventory.getAvailableGearList());
    }
}
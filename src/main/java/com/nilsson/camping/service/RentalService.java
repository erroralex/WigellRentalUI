package com.nilsson.camping.service;

import com.nilsson.camping.model.NewRentalResult;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.registries.RentalRegistry;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.NewRentalDialog;

public class RentalService {

    private final RentalRegistry rentalRegistry = RentalRegistry.getInstance();
    private final InventoryService inventoryService = new InventoryService();
    private final ProfitsService profitsService = new ProfitsService();

    /**
     * Handles the creation of a new rental through a dialog.
     * Records the profit upon successful rental creation.
     * @return The newly created Rental object, or null if cancelled or failed.
     */
    public Rental handleNewRental() {
        NewRentalDialog dialog = new NewRentalDialog();

        // Show the dialog and wait for the result
        NewRentalResult result = dialog.showAndWait().orElse(null);

        if (result != null) {

            IRentable item;
            if (result.getSelectedVehicle() != null) {
                item = result.getSelectedVehicle();
            } else if (result.getSelectedGear() != null) {
                item = result.getSelectedGear();
            } else {
                UIUtil.showErrorAlert("Error", "No Item Selected", "A rental must include a vehicle or gear item.");
                return null;
            }

            // Create the Rental object
            Rental newRental = new Rental(
                    rentalRegistry.getNextId(),
                    result.getSelectedMember(),
                    item,
                    result.getStartDate(),
                    result.getDays()
            );

            // Add rental to registry and mark item as rented
            rentalRegistry.addRental(newRental);
            item.setRented(true);

            // Saves changes to Inventory
            inventoryService.saveAllInventory();

            // Record the profit for the rental period
            profitsService.recordNewRentalProfit(result);

            return newRental;
        }
        return null;
    }

    /**
     * Handles the return of a rental item.
     * @param rental The rental to be processed for return.
     * @return true if the rental was successfully removed and item was marked available.
     */
    public boolean handleReturnRental(Rental rental) {

        // Find the rentable item to mark it as available
        IRentable item = inventoryService.findRentableItem(rental.getItemType(), rental.getItemName());

        if (item == null) {
            UIUtil.showErrorAlert("Item Not Found", "Data Mismatch", "The rented item could not be found in inventory.");
            return false;
        }

        // Mark the item as available
        item.setRented(false);

        // Remove the rental record
        boolean removed = rentalRegistry.removeRental(rental);

        if (removed) {
            // Saves the changes to Inventory and Rental Registry
            inventoryService.saveAllInventory();
            rentalRegistry.saveRentals();
        }

        return removed;
    }
}
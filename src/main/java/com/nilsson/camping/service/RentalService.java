package com.nilsson.camping.service;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.model.registries.RentalRegistry;
import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalRegistry rentalRegistry = RentalRegistry.getInstance();
    private final Inventory inventory = Inventory.getInstance();
    private final MemberRegistry memberRegistry = MemberRegistry.getInstance();

    // New Rental
    public boolean handleNewRental(Member member, IRentable item, LocalDate startDate, int rentalDays) {
        if (member == null || item == null || rentalDays <= 0) {
            return false;
        }

        int newRentalId = rentalRegistry.getNextId();

        Rental rental = new Rental(
                newRentalId,
                member,
                item,
                startDate,
                rentalDays
        );

        // Mark item as rented.
        item.setRented(true);

        // Add to registry.
        rentalRegistry.getRentals().add(rental);

        // Save Rentals and Inventory.
        DataHandler.saveRentals(rentalRegistry.getRentals());
        DataHandler.saveRecreationalVehicle(inventory.getRecreationalVehicleList());
        DataHandler.saveGear(inventory.getGearList());

        return true;
    }

    // Return Rental
    public boolean handleReturnRental(Rental rental) {
        if (rental == null) return false;

        IRentable item = (IRentable) inventory.findItemById(rental.getItemId());
        if (item != null) {
            item.setRented(false);
        }

        // Remove rental
        boolean removed = rentalRegistry.getRentals().remove(rental);

        // Save Rentals and Inventory.
        DataHandler.saveRentals(rentalRegistry.getRentals());
        DataHandler.saveRecreationalVehicle(inventory.getRecreationalVehicleList());
        DataHandler.saveGear(inventory.getGearList());

        return removed;
    }

    // Get all rentals.
    public List<Rental> getAllRentals() {
        return rentalRegistry.getRentals();
    }

    // Get Item name from Item ID.
    public String getItemNameFromId(int itemId) {
        IRentable item = (IRentable) inventory.findItemById(itemId);
        if (item == null) return "Unknown Item";
        return item.getItemName() + " (" + item.getItemType() + ")";
    }

    // Get Member name from Member ID.
    public String getMemberNameFromId(int memberId) {
        Member member = memberRegistry.findMemberById(memberId);
        if (member == null) return "Unknown Member";
        return member.getFirstName() + " " + member.getLastName();
    }
}
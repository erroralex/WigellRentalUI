package com.nilsson.camping.model.registries;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.Rental;
import java.util.ArrayList;
import java.util.List;

public class RentalRegistry {

    private List<Rental> rentals = new ArrayList<>();
    private int lastId = 0;

    private RentalRegistry() {
        loadRentalsFromDataHandler();
        for (Rental r : rentals) {
            if (r.getRentalId() > lastId) {
                lastId = r.getRentalId();
            }
        }
    }

    public int getNextId() {
        lastId++;
        return lastId;
    }

    public static RentalRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RentalRegistry INSTANCE = new RentalRegistry();
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
        DataHandler.saveRentals(rentals);
    }

    public boolean removeRental(Rental rental) {
        boolean removed = rentals.remove(rental);
        if (removed) {
            DataHandler.saveRentals(rentals);
        }
        return removed;
    }

    private void loadRentalsFromDataHandler() {
        this.rentals = DataHandler.loadRentals();
    }

    // Saves the current list of all rentals to .json via the DataHandler.
    public void saveRentals() {
        DataHandler.saveRentals(this.rentals);
    }
}
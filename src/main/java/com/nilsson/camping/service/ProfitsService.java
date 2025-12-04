package com.nilsson.camping.service;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.data.ProfitsHandler;
import com.nilsson.camping.model.DailyProfit;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.NewRentalResult;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.policies.PremiumPricePolicy;
import com.nilsson.camping.model.policies.StudentPricePolicy;
import com.nilsson.camping.model.policies.StandardPricePolicy;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.model.registries.MemberRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProfitsService {

    private final InventoryService inventoryService = new InventoryService();
    private ObservableList<DailyProfit> dailyProfits = FXCollections.observableArrayList(ProfitsHandler.loadProfits());

    public List<DailyProfit> getDailyProfits() {
        return dailyProfits;
    }

    public void setDailyProfits(ObservableList<DailyProfit> list) {
        this.dailyProfits = list;
    }

    public ObservableList<DailyProfit> getObservableDailyProfits() {
        return dailyProfits;
    }

    public double calculateDailyRate(Member member, IRentable rentable) {
        double dailyPrice = rentable.getDailyPrice();

        if (member.isStudent()) {
            return new StudentPricePolicy(dailyPrice).calculatePrice(1);
        } else if (member.isPremium()) {
            return new PremiumPricePolicy(dailyPrice).calculatePrice(1);
        } else {
            return new StandardPricePolicy(dailyPrice).calculatePrice(1);
        }
    }

    public void recordNewRentalProfit(NewRentalResult result) {
        IRentable item = result.getSelectedVehicle() != null ?
                result.getSelectedVehicle() : result.getSelectedGear();

        if (item == null) return;

        double calculatedDailyRate = calculateDailyRate(result.getSelectedMember(), item);
        LocalDate startDate = result.getStartDate();
        int rentalDays = result.getDays();

        for (int i = 0; i < rentalDays; i++) {
            LocalDate date = startDate.plusDays(i);
            addOrUpdateDailyProfit(date, calculatedDailyRate);
        }

        ProfitsHandler.saveProfits(dailyProfits);
    }

    private void addOrUpdateDailyProfit(LocalDate date, double income) {
        for (DailyProfit dailyProfit : dailyProfits) {
            if (dailyProfit.getDate().equals(date)) {
                dailyProfit.setIncome(dailyProfit.getIncome() + income);
                return;
            }
        }
        dailyProfits.add(new DailyProfit(date, income));
    }

    public double getIncomeToday() {
        LocalDate today = LocalDate.now();

        for (DailyProfit dailyProfit : dailyProfits) {
            if (dailyProfit.getDate().isEqual(today)) {
                return dailyProfit.getIncome();
            }
        }
        return 0.0;
    }

    /**
     * Recalculates profits from all active rentals in rentals.json,
     * merges with existing historical profits, and fills missing days (last 14 days only).
     */
    public void recalculateProfitsFromRentals() {
        List<Rental> rentals = DataHandler.loadRentals();

        // Clear old profits to avoid duplicates
        dailyProfits.clear();

        System.out.println("Processing " + rentals.size() + " active rentals - cleared old profits");

        for (Rental rental : rentals) {
            try {
                final Rental currentRental = rental;
                LocalDate startDate = LocalDate.parse(currentRental.getStartDate());
                System.out.println("Processing Rental ID " + rental.getRentalId() + " from " + startDate);

                // Member name matching
                String rentalMemberName = currentRental.getMemberName().trim();
                System.out.println("  Looking for member: '" + rentalMemberName + "'");

                Member member = MemberRegistry.getInstance().getMembers().stream()
                        .filter(m -> {
                            String fullName = (m.getFirstName() + " " + m.getLastName()).trim();
                            return fullName.equalsIgnoreCase(rentalMemberName) ||
                                    rentalMemberName.equalsIgnoreCase(m.getFirstName()) ||
                                    rentalMemberName.contains(m.getFirstName());
                        })
                        .findFirst().orElse(null);

                if (member == null) {
                    System.out.println("  Member not found for rental " + rental.getRentalId());
                    continue;
                }
                System.out.println("  Found member: " + member.getFirstName() + " " + member.getLastName());

                IRentable item = findRentableItemAll(currentRental.getItemType(), currentRental.getItemName());
                if (item == null) {
                    System.out.println("  Item not found for rental " + rental.getRentalId() + " (type: " + currentRental.getItemType() + ", name: " + currentRental.getItemName() + ")");
                    continue;
                }

                double dailyRate = calculateDailyRate(member, item);
                System.out.println("  Daily rate: " + dailyRate + " for " + currentRental.getRentalDays() + " days");

                // Only process days up to today
                for (int i = 0; i < currentRental.getRentalDays() && !startDate.plusDays(i).isAfter(LocalDate.now()); i++) {
                    LocalDate date = startDate.plusDays(i);
                    final LocalDate currentDate = date;
                    addOrUpdateDailyProfit(currentDate, dailyRate);
                }
            } catch (Exception e) {
                System.err.println("Error processing rental " + rental.getRentalId() + ": " + e.getMessage());
            }
        }

        // Fill missing days
        LocalDate maxDate = LocalDate.now();
        LocalDate minDate = maxDate.minusDays(14);

        for (LocalDate date = minDate; !date.isAfter(maxDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            if (dailyProfits.stream().noneMatch(p -> p.getDate().equals(currentDate))) {
                dailyProfits.add(new DailyProfit(currentDate, 0.0));
            }
        }

        // Sort
        dailyProfits.sort(Comparator.comparing(DailyProfit::getDate));
        List<DailyProfit> uniqueProfits = new ArrayList<>();
        for (DailyProfit profit : dailyProfits) {
            boolean exists = uniqueProfits.stream()
                    .anyMatch(p -> p.getDate().equals(profit.getDate()));
            if (!exists) {
                uniqueProfits.add(profit);
            }
        }
        dailyProfits.setAll(uniqueProfits);

        ProfitsHandler.saveProfits(dailyProfits);
        System.out.println("Profits updated: " + dailyProfits.size() + " total records (last 14 days)");
    }

    // Finds rentable item from all inventory.
    private IRentable findRentableItemAll(String itemType, String itemName) {
        Inventory inventory = Inventory.getInstance();
        String searchName = itemName.toLowerCase().trim();

        System.out.println("    Searching inventory for: '" + itemType + "' '" + itemName + "'");

        // Matching for vehicles
        for (RecreationalVehicle vehicle : inventory.getRecreationalVehicleList()) {
            String vehicleName = vehicle.getItemName().toLowerCase().trim();
            if (vehicle.getItemType().equals(itemType) &&
                    (vehicleName.equals(searchName) ||
                            searchName.contains(vehicleName) ||
                            vehicleName.contains(searchName) ||
                            (searchName.contains("adria") && vehicleName.contains("adria")) ||
                            (searchName.contains("coral") && vehicleName.contains("coral")) ||
                            (searchName.contains("california") && vehicleName.contains("california")) ||
                            (searchName.contains("tucson") && vehicleName.contains("tucson")))) {
                System.out.println("      MATCHED Vehicle: '" + vehicle.getItemName() + "'");
                return vehicle;
            }
        }

        // Matching for gear
        for (Gear gear : inventory.getGearList()) {
            String gearName = gear.getItemName().toLowerCase().trim();
            if (gear.getItemType().equals(itemType) &&
                    (gearName.equals(searchName) ||
                            searchName.contains(gearName) ||
                            gearName.contains(searchName) ||
                            (searchName.contains("tent") && gearName.contains("tent")) ||
                            (searchName.contains("backpack") && gearName.contains("backpack")) ||
                            (searchName.contains("chair") && gearName.contains("chair")))) {
                System.out.println("      MATCHED Gear: '" + gear.getItemName() + "'");
                return gear;
            }
        }

        // Default pricing
        System.out.println("      NO MATCH - using default pricing for " + itemType);
        if ("Vehicle".equals(itemType)) {
            return new DummyVehicle(1200.0);
        } else {
            return new DummyGear(250.0);
        }
    }

    // Fallback pricing
    private static class DummyVehicle implements IRentable {
        private double dailyPrice;
        DummyVehicle(double dailyPrice) { this.dailyPrice = dailyPrice; }
        public double getDailyPrice() { return dailyPrice; }
        public boolean isRented() { return false; }
        public void setRented(boolean rented) {}
        public String getItemType() { return "Vehicle"; }
        public String getItemName() { return "Unknown Vehicle"; }
    }

    private static class DummyGear implements IRentable {
        private double dailyPrice;
        DummyGear(double dailyPrice) { this.dailyPrice = dailyPrice; }
        public double getDailyPrice() { return dailyPrice; }
        public boolean isRented() { return false; }
        public void setRented(boolean rented) {}
        public String getItemType() { return "Gear"; }
        public String getItemName() { return "Unknown Gear"; }
    }
}
package com.nilsson.camping.service;

import com.nilsson.camping.data.ProfitsHandler;
import com.nilsson.camping.model.DailyProfit;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.NewRentalResult;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.policies.PremiumPricePolicy;
import com.nilsson.camping.model.policies.StudentPricePolicy;
import com.nilsson.camping.model.policies.StandardPricePolicy;
import java.time.LocalDate;
import java.util.List;

public class ProfitsService {

    private List<DailyProfit> dailyProfits = ProfitsHandler.loadProfits();

    /**
     * Calculates the actual daily price for a rented item based on the membership-level.
     * This uses the IPricePolicy implementation (Standard, Student, Premium).
     * @param member The member who rented the item.
     * @param rentable The item being rented, RecreationalVehicle or Gear.
     * @return The calculated daily rate.
     */
    public double calculateDailyRate(Member member, IRentable rentable) {

        double dailyPrice = rentable.getDailyPrice();

        if (member.isStudent()) {
            // Calculate the price for 1 day using the Student policy
            return new StudentPricePolicy(dailyPrice).calculatePrice(1);
        } else if (member.isPremium()) {
            // Calculate the price for 1 day using the Premium policy
            return new PremiumPricePolicy(dailyPrice).calculatePrice(1);
        } else {
            // Calculate the price for 1 day using the Standard policy
            return new StandardPricePolicy(dailyPrice).calculatePrice(1);
        }
    }

    /**
     * Records the income from a new rental, distributing the total price
     * over the rental days for accurate daily profit tracking.
     * @param result The result object containing rental details.
     */
    public void recordNewRentalProfit(NewRentalResult result) {
        // Item being rented
        IRentable item = result.getSelectedVehicle() != null ?
                result.getSelectedVehicle() : result.getSelectedGear();

        if (item == null) return;

        // Calculate the actual daily rate adjusted by the membership-level
        double calculatedDailyRate = calculateDailyRate(result.getSelectedMember(), item);
        LocalDate startDate = result.getStartDate();
        int rentalDays = result.getDays();

        // Iterate over each day of the rental period and update the profit
        for (int i = 0; i < rentalDays; i++) {
            LocalDate date = startDate.plusDays(i);
            addOrUpdateDailyProfit(date, calculatedDailyRate);
        }
        // Save the updated list
        ProfitsHandler.saveProfits(dailyProfits);
    }

    /**
     * Helper to update an existing daily record or create a new one.
     * @param date The date for which to record income.
     * @param income The amount of income to add for that date.
     */
    private void addOrUpdateDailyProfit(LocalDate date, double income) {
        // Check if a record for this date already exists
        for (DailyProfit dailyProfit : dailyProfits) {
            if (dailyProfit.getDate().equals(date)) {
                // Found existing record, update income and return
                dailyProfit.setIncome(dailyProfit.getIncome() + income);
                return;
            }
        }
        // If the date wasn't found, add a new record
        dailyProfits.add(new DailyProfit(date, income));
    }

    // Finds the recorded income for the current system date.
    public double getIncomeToday() {
        LocalDate today = LocalDate.now();

        // Iterate through the list of daily profits
        for (DailyProfit dailyProfit : dailyProfits) {
            // Check if the record's date matches today's date
            if (dailyProfit.getDate().isEqual(today)) {
                return dailyProfit.getIncome();
            }
        }
        // If today's date is not found in the records, return 0.0
        return 0.0;
    }

    public List<DailyProfit> getDailyProfits() {
        return dailyProfits;
    }
}
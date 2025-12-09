package com.nilsson.camping.service;

import com.nilsson.camping.model.policies.IPricePolicy;
import com.nilsson.camping.model.policies.PremiumPricePolicy;
import com.nilsson.camping.model.policies.StandardPricePolicy;
import com.nilsson.camping.model.policies.StudentPricePolicy;
import com.nilsson.camping.data.ProfitsHandler;
import com.nilsson.camping.model.DailyProfit;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.IRentable;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.model.registries.RentalRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfitsService {

    private final RentalRegistry rentalRegistry = RentalRegistry.getInstance();
    private final Inventory inventory = Inventory.getInstance();
    private final MemberRegistry memberRegistry = MemberRegistry.getInstance();
    private final ObservableList<DailyProfit> dailyProfits = FXCollections.observableArrayList(ProfitsHandler.loadProfits());

    public ObservableList<DailyProfit> getObservableDailyProfits() {
        return dailyProfits;
    }

    public List<DailyProfit> getDailyProfits() {
        return dailyProfits;
    }

    // Total income.
    public double calculateTotalIncome() {
        return rentalRegistry.getRentals().stream()
                .mapToDouble(this::calculateRentalRevenue)
                .sum();
    }

    // Income today.
    public double getIncomeToday() {
        return dailyProfits.stream()
                .filter(p -> p.getDate().isEqual(LocalDate.now()))
                .mapToDouble(DailyProfit::getIncome)
                .findFirst()
                .orElse(0.0);
    }


    public void recalculateProfitsFromRentals() {

        Map<LocalDate, Double> profitsMap = new HashMap<>();

        for (Rental rental : rentalRegistry.getRentals()) {

            IRentable item = (IRentable) inventory.findItemById(rental.getItemId());
            if (item == null) {
                System.err.println("ProfitsService: Skipped rental " + rental.getRentalId() +
                        " due to unknown item ID " + rental.getItemId());
                continue;
            }

            double dailyPrice = item.getDailyPrice();
            LocalDate startDate = rental.getStartDate();
            LocalDate endDate = startDate.plusDays(rental.getRentalDays() - 1);

            // Iterate through every day of the rental.
            LocalDate currentDay = startDate;
            while (!currentDay.isAfter(endDate)) {
                // Add the daily price to the map for the specific date.
                profitsMap.put(
                        currentDay,
                        profitsMap.getOrDefault(currentDay, 0.0) + dailyPrice
                );
                currentDay = currentDay.plusDays(1);
            }
        }

        // Convert the map to a list of DailyProfit
        List<DailyProfit> newProfits = profitsMap.entrySet().stream()
                .map(entry -> new DailyProfit(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyProfit::getDate))
                .collect(Collectors.toList());

        // Update the observable list and save
        dailyProfits.setAll(newProfits);
        ProfitsHandler.saveProfits(dailyProfits);
    }

    // Revenue for a specific rental.
    public double calculateRentalRevenue(Rental rental) {

        IRentable item = (IRentable) inventory.findItemById(rental.getItemId());
        if (item == null) {
            System.err.println("Warning: Rental " + rental.getRentalId() +
                    " references missing itemId " + rental.getItemId());
            return 0;
        }

        // Check membership level
        Member member = memberRegistry.findMemberById(rental.getMemberId());
        String level = (member != null) ? member.getMembershipLevel() : "Standard";

        // Which pricing policy to use
        IPricePolicy policy;
        double dailyRate = item.getDailyPrice();

        switch (level) {
            case "Student":
                policy = new StudentPricePolicy(dailyRate);
                break;
            case "Premium":
                policy = new PremiumPricePolicy(dailyRate);
                break;
            default:
                policy = new StandardPricePolicy(dailyRate);
                break;
        }
        return policy.calculatePrice(rental.getRentalDays());
    }

    // Revenue from a specific member (by memberId).
    public double calculateMemberRevenue(int memberId) {
        return rentalRegistry.getRentals().stream()
                .filter(r -> r.getMemberId() == memberId)
                .mapToDouble(this::calculateRentalRevenue)
                .sum();
    }

    // Revenue per member.
    public String generateMemberRevenueReport() {
        StringBuilder sb = new StringBuilder();

        for (Member member : memberRegistry.getMembers()) {
            double revenue = calculateMemberRevenue(member.getId());
            sb.append(String.format("%s %s: %.2f SEK%n",
                    member.getFirstName(),
                    member.getLastName(),
                    revenue));
        }

        return sb.toString();
    }
}
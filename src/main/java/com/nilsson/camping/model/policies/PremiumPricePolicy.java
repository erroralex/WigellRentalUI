package com.nilsson.camping.model.policies;

public class PremiumPricePolicy implements IPricePolicy {

    private final StandardPricePolicy standardPricePolicy;
    private final double PREMIUM_RATE = 1.2;

    public PremiumPricePolicy(double dailyRate) {
        this.standardPricePolicy = new StandardPricePolicy(dailyRate);
    }

    @Override
    public double calculatePrice(int days) {
        return standardPricePolicy.calculatePrice(days) * PREMIUM_RATE;
    }
}
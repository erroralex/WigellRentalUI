package com.nilsson.camping.model.policies;

public class StandardPricePolicy implements IPricePolicy {

    private double dailyRate;

    public StandardPricePolicy(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public double calculatePrice(int days) {
        return days * dailyRate;
    }
}
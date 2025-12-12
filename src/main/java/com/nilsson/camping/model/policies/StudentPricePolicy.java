package com.nilsson.camping.model.policies;

public class StudentPricePolicy implements IPricePolicy {

    private final StandardPricePolicy standardPricePolicy;
    private final double STUDENT_DISCOUNT = 0.8;

    public StudentPricePolicy(double dailyRate) {
        this.standardPricePolicy = new StandardPricePolicy(dailyRate);
    }

    @Override
    public double calculatePrice(int days) {
        return standardPricePolicy.calculatePrice(days) * STUDENT_DISCOUNT;
    }
}
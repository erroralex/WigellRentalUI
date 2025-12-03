package com.nilsson.camping.model.items;

public abstract class Item {

    private double dailyPrice;

    public Item() {

    }

    public Item(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }
}
package com.nilsson.camping.model;

import java.time.LocalDate;

public class DailyProfit {

    private LocalDate date;
    private double income;

    public DailyProfit() {

    }

    public DailyProfit(LocalDate date, double income) {
        this.date = date;
        this.income = income;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
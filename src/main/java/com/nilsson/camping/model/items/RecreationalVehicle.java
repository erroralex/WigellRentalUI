package com.nilsson.camping.model.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nilsson.camping.app.LanguageManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecreationalVehicle extends Vehicle implements IRentable {

    private String capacity;
    private String type;
    private boolean rented;

    public RecreationalVehicle() { super(); }

    public RecreationalVehicle(int itemId, String make, String model, String type, double dailyPrice, String year, String capacity) {
        super(itemId, make, model, dailyPrice, year);
        this.capacity = capacity;
        this.type = type;
    }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRented() { return rented; }
    public void setRented(boolean rented) { this.rented = rented; }

    @Override
    public String toString() {
        String status = this.isRented() ? " " +
                LanguageManager.getInstance().getString("status.rented") : " " +
                LanguageManager.getInstance().getString("status.available");
        return String.format("%s %s (%s, %s) - Daily Price: %.2f SEK%s",
                this.getMake(),
                this.getModel(),
                this.getType(),
                this.getYear(),
                this.getDailyPrice(),
                status);
    }

    @Override
    public String getItemType() { return getType(); }

    @Override
    public String getItemName() { return getMake() + " " + getModel(); }
}
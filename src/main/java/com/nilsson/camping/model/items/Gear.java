package com.nilsson.camping.model.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nilsson.camping.app.LanguageManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gear extends Item implements IRentable {

    private String model;
    private String type;
    private String capacity;
    private boolean rented;

    public Gear() {
        super();
    }

    public Gear(int itemId, String model, String type, String capacity, double dailyPrice) {
        super(itemId, dailyPrice);
        this.model = model;
        this.type = type;
        this.capacity = capacity;
    }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRented() { return rented; }
    public void setRented(boolean rented) { this.rented = rented; }

    @Override
    public String toString() {
        String status = this.isRented() ? " " +
                LanguageManager.getInstance().getString("status.rented") : " " +
                LanguageManager.getInstance().getString("status.available");
        return String.format("%s (%s) - Daily Price: %.2f SEK%s",
                this.getModel(),
                this.getType(),
                this.getDailyPrice(),
                status);
    }

    @Override
    public String getItemType() { return getType(); }

    @Override
    public String getItemName() { return getModel(); }
}
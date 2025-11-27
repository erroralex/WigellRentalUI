package com.nilsson.camping.model.items;

public class Gear extends Item {

    private String model;
    private String type;
    private String capacity;

    public Gear() {

    }

    public Gear(String model, double dailyPrice, String capacity) {
        super(dailyPrice);
        this.model = model;
        this.type = type;
        this.capacity = capacity;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
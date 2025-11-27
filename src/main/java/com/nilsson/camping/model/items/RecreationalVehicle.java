package com.nilsson.camping.model.items;

public class RecreationalVehicle extends Vehicle {

    private String capacity;
    private String type;

    public RecreationalVehicle() {

    }

    public RecreationalVehicle(String make, String model, String type, double dailyPrice, String year, String capacity) {
        super(make, model, dailyPrice, year);
        this.capacity = capacity;
        this.type = type;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
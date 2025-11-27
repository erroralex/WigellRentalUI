package com.nilsson.camping.model.registries;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<RecreationalVehicle> recreationalVehicleList = new ArrayList<>();
    private List<Gear> gearList = new ArrayList<>();

    private Inventory() {
        // Load data from JSON files on initialization
        loadGearFromDataHandler();
        loadRecreationalVehiclesFromDataHandler();
    }

    public Inventory(List<RecreationalVehicle> recreationalVehicleList, List<Gear> gearList) {
        this.recreationalVehicleList = recreationalVehicleList;
        this.gearList = gearList;
    }

    public static Inventory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Inventory INSTANCE = new Inventory();
    }

    public List<RecreationalVehicle> getRecreationalVehicleList() {
        return recreationalVehicleList;
    }

    public List<Gear> getGearList() {
        return gearList;
    }

    public void addRecreationalVehicle(RecreationalVehicle rv) {
        this.recreationalVehicleList.add(rv);
    }

    public void addGear(Gear gear) {
        this.gearList.add(gear);
    }

    public List<Vehicle> getVehicleList() {
        return new ArrayList<>(this.recreationalVehicleList);
    }

    // Loads vehicles using the DataHandler and populates the recreational vehicle list.
    private void loadRecreationalVehiclesFromDataHandler() {
        this.recreationalVehicleList = DataHandler.loadRecreationalVehicles();
    }

    // Loads gear using the DataHandler and populates the gear list.
    private void loadGearFromDataHandler() {
        List<Gear> loadedGear = DataHandler.loadGear();
        this.gearList = new ArrayList<>(loadedGear);
    }
}
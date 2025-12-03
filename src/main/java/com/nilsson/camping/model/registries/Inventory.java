package com.nilsson.camping.model.registries;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.items.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    private List<RecreationalVehicle> recreationalVehicleList = new ArrayList<>();
    private List<Gear> gearList = new ArrayList<>();

    private Inventory() {
        // Load data from JSON files
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

    public List<RecreationalVehicle> getAvailableRecreationalVehicleList() {
        return recreationalVehicleList.stream().filter(rv -> !rv.isRented())
                .collect(Collectors.toList());
    }

    public List<RecreationalVehicle> getRecreationalVehicleList() {
        return recreationalVehicleList;
    }

    public List<Gear> getGearList() {
        return gearList;
    }

    public List<Gear> getAvailableGearList() {
        return gearList.stream().filter(g -> !g.isRented())
                .collect(Collectors.toList());
    }

    public void addRecreationalVehicle(RecreationalVehicle rv) {
        this.recreationalVehicleList.add(rv);
        DataHandler.saveRecreationalVehicle(this.recreationalVehicleList);
    }

    public void addGear(Gear gear) {
        this.gearList.add(gear);
        DataHandler.saveGear(this.gearList);
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

    public boolean removeRecreationalVehicle(RecreationalVehicle rv) {
        boolean wasRemoved = this.recreationalVehicleList.remove(rv);

        if(wasRemoved) {
            DataHandler.saveRecreationalVehicle(this.recreationalVehicleList);
        }
        return wasRemoved;
    }

    public boolean removeGear(Gear gear) {

        boolean wasRemoved = this.gearList.remove(gear);

        if (wasRemoved) {
            DataHandler.saveGear(this.gearList);
        }

        return wasRemoved;
    }
}
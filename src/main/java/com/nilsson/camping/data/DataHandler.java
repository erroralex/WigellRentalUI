package com.nilsson.camping.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nilsson.camping.model.Rental;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.items.RecreationalVehicle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Handles persistent data operations using the Jackson library.
public class DataHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    // Absolute File Paths
    private static final String MEMBERS_PERSISTENCE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/data/json/members.json";

    private static final String VEHICLES_PERSISTENCE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/data/json/vehicles.json";

    private static final String GEAR_PERSISTENCE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/data/json/gear.json";

    private static final String RENTALS_PERSISTENCE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/data/json/rentals.json";

    // ──────────────────────────────────────────────────────
    //                      Member Operations
    // ──────────────────────────────────────────────────────

    // Load Members
    public static List<Member> loadMembers() {
        File file = new File(MEMBERS_PERSISTENCE_PATH);
        List<com.nilsson.camping.model.Member> members = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("INFO: Members file not found or is empty at " + MEMBERS_PERSISTENCE_PATH + ". Starting with empty list.");
            return members;
        }

        try {
            members = MAPPER.readValue(file, new TypeReference<List<com.nilsson.camping.model.Member>>() {});
            System.out.println("Successfully loaded " + members.size() + " members from " + MEMBERS_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load members data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    // Save Members
    public static void saveMembers(List<com.nilsson.camping.model.Member> members) {
        try {
            File file = new File(MEMBERS_PERSISTENCE_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writeValue(file, members);
            System.out.println("Successfully saved " + members.size() + " members to " + MEMBERS_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not save members data to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────
    //                  Vehicle Operations
    // ──────────────────────────────────────────────────────

    // Load Vehicles
    public static List<RecreationalVehicle> loadRecreationalVehicles() {
        File file = new File(VEHICLES_PERSISTENCE_PATH);
        List<RecreationalVehicle> recreationalVehicles = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("INFO: Vehicles file not found or is empty at " + VEHICLES_PERSISTENCE_PATH + ". Starting with empty list.");
            return recreationalVehicles;
        }

        try {
            recreationalVehicles = MAPPER.readValue(file, new TypeReference<List<RecreationalVehicle>>() {});
            System.out.println("Successfully loaded " + recreationalVehicles.size() + " vehicles from " + VEHICLES_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load vehicle data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return recreationalVehicles;
    }

    // Save Vehicles
    public static void saveRecreationalVehicle(List<RecreationalVehicle> recreationalVehicles) {
        try {
            File file = new File(VEHICLES_PERSISTENCE_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writeValue(file, recreationalVehicles);
            System.out.println("Successfully saved " + recreationalVehicles.size() + " vehicles to " + VEHICLES_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not save vehicle data to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────
    //                  Gear Operations
    // ──────────────────────────────────────────────────────

    // Load Gear
    public static List<Gear> loadGear() {
        File file = new File(GEAR_PERSISTENCE_PATH);
        List<Gear> gearList = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("INFO: Gear file not found or is empty at " + GEAR_PERSISTENCE_PATH + ". Starting with empty list.");
            return gearList;
        }

        try {
            gearList = MAPPER.readValue(file, new TypeReference<List<Gear>>() {});
            System.out.println("Successfully loaded " + gearList.size() + " items from " + GEAR_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load gear data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return gearList;
    }

    // Save Gear
    public static void saveGear(List<Gear> gearList) {
        try {
            File file = new File(GEAR_PERSISTENCE_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writeValue(file, gearList);
            System.out.println("Successfully saved " + gearList.size() + " gear to " + GEAR_PERSISTENCE_PATH);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not save gear data to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ──────────────────────────────────────────────────────
    //                      Rental Operations
    // ──────────────────────────────────────────────────────

    public static List<Rental> loadRentals() {
        File file = new File(RENTALS_PERSISTENCE_PATH);
        List<Rental> rentals = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("INFO: Rentals file not found or is empty at " +
                    RENTALS_PERSISTENCE_PATH + ". Starting with empty list.");
            return rentals;
        }

        try {
            rentals = MAPPER.readValue(file, new TypeReference<List<Rental>>() {
            });
            System.out.println("Successfully loaded " + rentals.size() +
                    " rentals from " + RENTALS_PERSISTENCE_PATH);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load rentals data from JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return rentals;
    }

    public static void saveRentals(List<Rental> rentals) {
        try {
            File file = new File(RENTALS_PERSISTENCE_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writeValue(file, rentals);
            System.out.println("Successfully saved " + rentals.size() +
                    " rentals to " + RENTALS_PERSISTENCE_PATH);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not save rentals data to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
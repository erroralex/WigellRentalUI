package com.nilsson.camping.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistent data operations, specifically loading and saving data
 * from/to JSON files using the Jackson library.
 */
public class DataHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String MEMBERS_FILE = "/members.json";
    private static final String VEHICLES_FILE = "/vehicles.json";
    private static final String GEAR_FILE = "/gear.json";

    // Loads the list of members from the JSON file located in the resources folder
    public static List<Member> loadMembers() {

        List<Member> members = new ArrayList<>();
        try (InputStream is = DataHandler.class.getResourceAsStream(MEMBERS_FILE)) {

            if (is == null) {
                System.err.println("ERROR: Resource file not found: " + MEMBERS_FILE + ". Ensure it's in the resources folder.");
                // Return empty list if file is missing
                return members;
            }

            members = MAPPER.readValue(is, new TypeReference<List<Member>>() {
            });
            System.out.println("Successfully loaded " + members.size() + " members from " + MEMBERS_FILE);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load members data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    // Loads the list of vehicles from the JSON file located in the resources folder
    public static List<RecreationalVehicle> loadRecreationalVehicles() {

        List<RecreationalVehicle> recreationalVehicles = new ArrayList<>();
        try (InputStream is = DataHandler.class.getResourceAsStream(VEHICLES_FILE)) {

            if (is == null) {
                System.err.println("ERROR: Resource file not found: " + VEHICLES_FILE + ". Ensure it's in the resources folder.");
                // Return empty list if file is missing
                return recreationalVehicles;
            }

            recreationalVehicles = MAPPER.readValue(is, new TypeReference<List<RecreationalVehicle>>() {
            });
            System.out.println("Successfully loaded " + recreationalVehicles.size() + " vehicles from " + VEHICLES_FILE);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load members data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return recreationalVehicles;
    }

    // Loads the list of gear from the JSON file located in the resources folder
    public static List<Gear> loadGear() {

        List<Gear> gear = new ArrayList<>();
        try (InputStream is = DataHandler.class.getResourceAsStream(GEAR_FILE)) {

            if (is == null) {
                System.err.println("ERROR: Resource file not found: " + GEAR_FILE + ". Ensure it's in the resources folder.");
                // Return empty list if file is missing
                return gear;
            }

            gear = MAPPER.readValue(is, new TypeReference<List<Gear>>() {
            });
            System.out.println("Successfully loaded " + gear.size() + " items from " + GEAR_FILE);

        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not load members data from JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return gear;
    }
}
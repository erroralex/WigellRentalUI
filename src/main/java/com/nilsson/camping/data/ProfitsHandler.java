package com.nilsson.camping.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nilsson.camping.model.DailyProfit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfitsHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule()).
            enable(SerializationFeature.INDENT_OUTPUT);

    private static final String PROFITS_PERSISTENCE_PATH = System.getProperty("user.dir") +
            "/src/main/resources/data/json/profits.json";

    public static List<DailyProfit> loadProfits() {
        File file = new File(PROFITS_PERSISTENCE_PATH);
        List<DailyProfit> profits = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            System.out.println("INFO: Profits file not found or empty. Starting with empty list.");
            return profits;
        }

        try {
            profits = MAPPER.readValue(file, new TypeReference<List<DailyProfit>>() {});
            System.out.println("Successfully loaded " + profits.size() + " profit records");
        } catch (IOException e) {
            System.out.println("FATAL ERROR: Could not load profits data from JSON: " + e.getMessage());
        }
        return profits;
    }

    public static void saveProfits(List<DailyProfit> profits) {
        try {
            File file = new File(PROFITS_PERSISTENCE_PATH);
            file.getParentFile().mkdirs();
            MAPPER.writeValue(file, profits);
            System.out.println("Successfully saved " + profits.size() + " profit records.");
        } catch (IOException e) {
            System.out.println("FATAL ERROR: Could not save profits data from JSON: " + e.getMessage());
        }
    }
}

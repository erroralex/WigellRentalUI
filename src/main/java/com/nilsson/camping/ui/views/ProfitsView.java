package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.DailyProfit;
import com.nilsson.camping.service.ProfitsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfitsView extends VBox {

    private final ProfitsService profitsService = new ProfitsService();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd");
    private final Label incomeTodayValueLabel = new Label();

    public ProfitsView() {

        // Apply CSS and Layout
        this.getStyleClass().add("content-view");
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Rental Income");
        title.getStyleClass().add("content-title");

        // Today's Income Display
        HBox incomeTodayBox = createIncomeTodayBox();

        // Total Income Display
        double totalIncome = profitsService.getDailyProfits().stream()
                .mapToDouble(DailyProfit::getIncome)
                .sum();

        // Create an HBox for the Total Recorded Income label.
        Label totalLabelDesc = new Label("Total Recorded Income:");
        totalLabelDesc.getStyleClass().add("income-stat-label");

        Label totalLabelValue = new Label(String.format("%,.2f SEK", totalIncome));
        totalLabelValue.getStyleClass().add("income-stat-value");

        HBox totalIncomeBox = new HBox(10, totalLabelDesc, totalLabelValue);
        totalIncomeBox.setAlignment(Pos.CENTER_LEFT);
        totalIncomeBox.getStyleClass().add("income-stats-box");

        BarChart<String, Number> incomeChart = createIncomeBarChart();

        this.getChildren().addAll(title, incomeTodayBox, totalIncomeBox, incomeChart);
    }

    // Creates the HBox for the Income Today label.
    private HBox createIncomeTodayBox() {

        // Retrieve data
        double incomeToday = profitsService.getIncomeToday();

        // Format the income string
        String formattedIncome = String.format("%,.2f SEK", incomeToday);

        // Description Label
        Label incomeTodayLabel = new Label("Income Today:");
        incomeTodayLabel.getStyleClass().add("income-stat-label");

        // Value Label
        incomeTodayValueLabel.setText(formattedIncome);
        incomeTodayValueLabel.getStyleClass().add("income-stat-value");

        // Add to Container
        HBox box = new HBox(10, incomeTodayLabel, incomeTodayValueLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("income-stats-box");

        return box;
    }

    // Bar Chart
    private BarChart<String, Number> createIncomeBarChart() {
        // Get and sort data by date
        List<DailyProfit> sortedProfits = profitsService.getDailyProfits().stream()
                .sorted(Comparator.comparing(DailyProfit::getDate))
                .collect(Collectors.toList());

        // Setup the chart X & Y
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Daily Income Over Time (SEK)");
        xAxis.setLabel("Date");
        yAxis.setLabel("Income (SEK)");
        barChart.setLegendVisible(false);

        // Data
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (DailyProfit profit : sortedProfits) {
            series.getData().add(new XYChart.Data<>(
                    profit.getDate().format(DATE_FORMATTER),
                    profit.getIncome()));
        }

        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList(series);
        barChart.setData(data);

        // CSS
        barChart.getStyleClass().add("profit-chart");

        return barChart;
    }
}
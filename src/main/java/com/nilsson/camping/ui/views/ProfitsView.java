package com.nilsson.camping.ui.views;

import com.nilsson.camping.model.DailyProfit;
import com.nilsson.camping.service.ProfitsService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfitsView extends VBox {

    private final ProfitsService profitsService = new ProfitsService();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd");
    private final Label incomeTodayValueLabel = new Label();
    private final XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();

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

        Label totalLabelDesc = new Label("Total Recorded Income:");
        totalLabelDesc.getStyleClass().add("income-stat-label");

        Label totalLabelValue = new Label(String.format("%,.2f SEK", totalIncome));
        totalLabelValue.getStyleClass().add("income-stat-value");

        HBox totalIncomeBox = new HBox(10, totalLabelDesc, totalLabelValue);
        totalIncomeBox.setAlignment(Pos.CENTER_LEFT);
        totalIncomeBox.getStyleClass().add("income-stats-box");

        // Create Bar Chart with dynamic Y-axis
        BarChart<String, Number> incomeChart = createIncomeBarChart();
        incomeChart.getData().add(profitSeries);

        // Auto-recalculate on load and populate chart data
        profitsService.recalculateProfitsFromRentals();
        updateChartData();

        // Live binding for Today's Income
        incomeTodayValueLabel.textProperty().bind(
                Bindings.createStringBinding(() ->
                                String.format("%.2f SEK", profitsService.getIncomeToday()),
                        profitsService.getObservableDailyProfits()
                )
        );

        // Refresh button
        Button refreshBtn = new Button("Refresh Profits");
        refreshBtn.setOnAction(e -> {
            profitsService.recalculateProfitsFromRentals();
            updateChartData();
        });
        refreshBtn.getStyleClass().add("action-button");

        // Add all to layout
        this.getChildren().addAll(title, incomeTodayBox, totalIncomeBox, refreshBtn, incomeChart);
    }

    private void updateChartData() {
        LocalDate today = LocalDate.now();
        LocalDate fourteenDaysAgo = today.minusDays(14);

        List<DailyProfit> recentProfits = profitsService.getDailyProfits().stream()
                .filter(p -> !p.getDate().isBefore(fourteenDaysAgo))
                .sorted(Comparator.comparing(DailyProfit::getDate))
                .collect(Collectors.toList());

        ObservableList<XYChart.Data<String, Number>> newData = FXCollections.observableArrayList();
        for (DailyProfit profit : recentProfits) {
            newData.add(new XYChart.Data<>(
                    profit.getDate().format(DATE_FORMATTER),
                    profit.getIncome()));
        }
        profitSeries.getData().setAll(newData);

        // Dynamic Y-axis
        if (!newData.isEmpty()) {
            double maxIncome = newData.stream()
                    .mapToDouble(data -> data.getYValue().doubleValue())
                    .max()
                    .orElse(0.0);

            // Scale to 120% of max
            double upperBound = Math.ceil(maxIncome * 1.2 / 1000.0) * 1000.0;
            NumberAxis yAxis = (NumberAxis) profitSeries.getChart().getYAxis();
            yAxis.setUpperBound(upperBound);
            yAxis.setTickUnit(upperBound / 5);
            yAxis.setAutoRanging(false);
        }
    }

    private HBox createIncomeTodayBox() {
        Label incomeTodayLabel = new Label("Income Today:");
        incomeTodayLabel.getStyleClass().add("income-stat-label");

        incomeTodayValueLabel.getStyleClass().add("income-stat-value");

        HBox box = new HBox(10, incomeTodayLabel, incomeTodayValueLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("income-stats-box");

        return box;
    }

    private BarChart<String, Number> createIncomeBarChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        // Enable auto-ranging
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Daily Income - Last 14 Days (SEK)");
        xAxis.setLabel("Date");
        yAxis.setLabel("Income (SEK)");
        barChart.setLegendVisible(false);

        barChart.getStyleClass().add("profit-chart");

        return barChart;
    }
}
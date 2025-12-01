package com.nilsson.camping.ui.views;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProfitsView extends VBox {

    public ProfitsView() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_LEFT);

        Label profitsLabel = new Label("Profits will be shown here:");
        PieChart profitChart = createSalesChart();

        this.getChildren().addAll(profitsLabel, profitChart);
    }

    private PieChart createSalesChart() {
        PieChart.Data slice1 = new PieChart.Data("Q1", 2000);
        PieChart.Data slice2 = new PieChart.Data("Q2", 4500);
        PieChart.Data slice3 = new PieChart.Data("Q3", 3000);

        PieChart chart = new PieChart(FXCollections.observableArrayList(slice1, slice2, slice3));
        chart.setTitle("Quarterly Sales");
        chart.setPrefSize(400, 300);
        return chart;
    }
}

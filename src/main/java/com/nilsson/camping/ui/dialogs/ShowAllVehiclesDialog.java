package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.items.RecreationalVehicle;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.ui.UIUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.util.List;

public class ShowAllVehiclesDialog extends Dialog<RecreationalVehicle> {

    public ShowAllVehiclesDialog() {

        // Dialog setup
        setTitle("All Vehicles");
        setHeaderText("All Vehicles in Inventory");

        this.setOnShowing(dialogEvent -> {
            UIUtil.applyDialogSetup(this);
            getDialogPane().getStyleClass().add("show-all-dialog");
        });

        getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);

        // ListView for presentation
        VBox content = new VBox(10);
        content.setPrefSize(700, 800);

        List<RecreationalVehicle> recreationalVehicleList = Inventory.getInstance().getRecreationalVehicleList();

        if (recreationalVehicleList == null || recreationalVehicleList.isEmpty()) {
            // No vehicles.
            content.getChildren().add(new javafx.scene.control.Label("No Vehicles was found in list."));
        } else {
            // Vehicles exists
            ListView<RecreationalVehicle> recreationalVehiclesListView = new ListView<>(FXCollections.observableArrayList(recreationalVehicleList));

            // Add custom style class
            recreationalVehiclesListView.getStyleClass().add("show-all-view");

            // Allow the list view to grow to fill the VBox
            VBox.setVgrow(recreationalVehiclesListView, javafx.scene.layout.Priority.ALWAYS);

            content.getChildren().add(recreationalVehiclesListView);
        }

        getDialogPane().setContent(content);
        setResizable(true);
    }
}
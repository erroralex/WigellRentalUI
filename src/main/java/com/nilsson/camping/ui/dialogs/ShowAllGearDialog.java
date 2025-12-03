package com.nilsson.camping.ui.dialogs;

import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.registries.Inventory;
import com.nilsson.camping.ui.UIUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.util.List;

public class ShowAllGearDialog extends Dialog<Gear> {

    public ShowAllGearDialog() {

        // Dialog setup
        setTitle("All Gear");
        setHeaderText("All Gear in Inventory");

        this.setOnShowing(dialogEvent -> {
            UIUtil.applyDialogSetup(this);
            getDialogPane().getStyleClass().add("show-all-dialog");
        });

        getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);

        // ListView for presentation
        VBox content = new VBox(10);
        content.setPrefSize(700, 800);

        List<Gear> gearList = Inventory.getInstance().getGearList();

        if (gearList == null || gearList.isEmpty()) {
            // No gear.
            content.getChildren().add(new javafx.scene.control.Label("No Gear was found in list."));
        } else {
            // Gear exists
            ListView<Gear> gearListView = new ListView<>(FXCollections.observableArrayList(gearList));

            // Add custom style class
            gearListView.getStyleClass().add("show-all-view");

            // Allow the list view to grow to fill the VBox
            VBox.setVgrow(gearListView, javafx.scene.layout.Priority.ALWAYS);

            content.getChildren().add(gearListView);
        }

        getDialogPane().setContent(content);
        setResizable(true);
    }
}
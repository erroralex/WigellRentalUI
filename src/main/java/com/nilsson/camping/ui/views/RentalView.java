package com.nilsson.camping.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RentalView extends VBox {


    public RentalView() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_LEFT);

        Label profitsLabel = new Label("Rentals will be shown here:");

        this.getChildren().addAll(profitsLabel);
    }
}

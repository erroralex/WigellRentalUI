package com.nilsson.camping.model.items;

public interface IRentable {

    double getDailyPrice();
    boolean isRented();
    void setRented(boolean rented);

    String getItemType();
    String getItemName();
}

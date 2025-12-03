package com.nilsson.camping.model;

import com.nilsson.camping.model.items.IRentable;
import java.time.LocalDate;

public class Rental {

    private int rentalId;
    private String memberName;
    private String itemName;
    private String itemType;
    private String startDate;
    private int rentalDays;

    public Rental() {
    }

    public Rental(int rentalId, String memberName, String itemName, String itemType, String startDate, int rentalDays) {
        this.rentalId = rentalId;
        this.memberName = memberName;
        this.itemName = itemName;
        this.itemType = itemType;
        this.startDate = startDate;
        this.rentalDays = rentalDays;
    }

    public Rental(int rentalId, Member member, IRentable item, LocalDate startDate, int rentalDays) {
        this.rentalId = rentalId;
        this.memberName = member.getFirstName() + " " + member.getLastName();
        this.itemName = item.getItemName();
        this.itemType = item.getItemType();
        this.startDate = startDate.toString();
        this.rentalDays = rentalDays;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }
}
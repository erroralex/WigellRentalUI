package com.nilsson.camping.model;

import java.time.LocalDate;
import com.nilsson.camping.model.items.IRentable;

public class Rental {

    private int rentalId;
    private int memberId;
    private int itemId;
    private LocalDate startDate;
    private int rentalDays;

    public Rental() { }


    public Rental(int rentalId, String memberName, String itemName, String itemType, LocalDate startDate, int rentalDays) {
        this.rentalId = rentalId;
        this.startDate = startDate;
        this.rentalDays = rentalDays;
    }

    public Rental(int rentalId, Member member, IRentable item, LocalDate startDate, int rentalDays) {
        this.rentalId = rentalId;
        this.memberId = member.getId();
        this.itemId = item.getItemId();
        this.startDate = startDate;
        this.rentalDays = rentalDays;
    }

    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; }
}
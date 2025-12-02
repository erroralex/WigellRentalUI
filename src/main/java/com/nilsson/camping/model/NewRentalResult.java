package com.nilsson.camping.model;

import com.nilsson.camping.model.items.Gear;
import com.nilsson.camping.model.items.RecreationalVehicle;

import java.time.LocalDate;

public class NewRentalResult {
    private final Member member;
    private final Gear gear;
    private final RecreationalVehicle vehicle;
    private final LocalDate startDate;
    private final int days;

    public NewRentalResult(Member member,
                           Gear gear,
                           RecreationalVehicle vehicle,
                           LocalDate startDate,
                           int days) {
        this.member = member;
        this.gear = gear;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.days = days;
    }

    public Member getSelectedMember() { return member; }
    public Gear getSelectedGear() { return gear; }
    public RecreationalVehicle getSelectedVehicle() { return vehicle; }
    public LocalDate getStartDate() { return startDate; }
    public int getDays() { return days; }
}


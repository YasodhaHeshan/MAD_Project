package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "buses",
        foreignKeys = {
                @ForeignKey(entity = BusOwner.class,
                        parentColumns = "id",
                        childColumns = "owner_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "owner_id")
        })
public class Bus {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "owner_id")
    private int ownerId;

    @ColumnInfo(name = "registration_number")
    private String registrationNumber;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "total_seats")
    private int totalSeats;

    @ColumnInfo(name = "amenities")
    private String amenities;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "route_from")
    private String routeFrom;

    @ColumnInfo(name = "route_to")
    private String routeTo;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    public Bus(int ownerId, String registrationNumber, String model, int totalSeats, String amenities, boolean isActive, String routeFrom, String routeTo) {
        this.ownerId = ownerId;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.totalSeats = totalSeats;
        this.amenities = amenities;
        this.isActive = isActive;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
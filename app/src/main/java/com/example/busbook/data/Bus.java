package com.example.busbook.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "buses",
        foreignKeys = {
                @ForeignKey(entity = BusOwner.class,
                            parentColumns = "id",
                            childColumns = "bus_owner_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = BusDriver.class,
                            parentColumns = "id",
                            childColumns = "bus_driver_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Ticket.class,
                            parentColumns = "id",
                            childColumns = "ticket_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Route.class,
                            parentColumns = "id",
                            childColumns = "route_id",
                            onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "bus_owner_id"),
                @Index(value = "bus_driver_id"),
                @Index(value = "ticket_id"),
                @Index(value = "route_id")
        })
public class Bus {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bus_number")
    private String busNumber;

    @ColumnInfo(name = "bus_owner_id")
    private int busOwnerId;

    @ColumnInfo(name = "bus_driver_id")
    private int busDriverId;

    @ColumnInfo(name = "ticket_id")
    private int ticketId;

    @ColumnInfo(name = "route_id")
    private int routeId;

    @ColumnInfo(name = "departure_location")
    private String departureLocation;

    @ColumnInfo(name = "arrival_location")
    private String arrivalLocation;

    @ColumnInfo(name = "departure_time")
    private String departureTime;

    @ColumnInfo(name = "arrival_time")
    private String arrivalTime;

    @ColumnInfo(name = "available_seats")
    private int availableSeats;

    public Bus(String busNumber, int busOwnerId, int busDriverId, int ticketId, int routeId, String departureLocation, String arrivalLocation, String departureTime, String arrivalTime, int availableSeats) {
        this.busNumber = busNumber;
        this.busOwnerId = busOwnerId;
        this.busDriverId = busDriverId;
        this.ticketId = ticketId;
        this.routeId = routeId;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public int getBusOwnerId() {
        return busOwnerId;
    }

    public void setBusOwnerId(int busOwnerId) {
        this.busOwnerId = busOwnerId;
    }

    public int getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(int busDriverId) {
        this.busDriverId = busDriverId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
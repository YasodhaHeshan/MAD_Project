package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bus_drivers",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "user_id"))
public class BusDriver {
    @PrimaryKey(autoGenerate = true)
    private int id;

    
    @ColumnInfo(name = "user_id")
    private int userId;

    
    @ColumnInfo(name = "license_number")
    private String licenseNumber;

    
    @ColumnInfo(name = "nic")
    private String nic;

    public BusDriver(int userId,  String licenseNumber,  String nic) {
        this.userId = userId;
        this.licenseNumber = licenseNumber;
        this.nic = nic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber( String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    
    public String getNic() {
        return nic;
    }

    public void setNic( String nic) {
        this.nic = nic;
    }
}
package com.zimpassflow.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vehicles")
public class Vehicle {
    @PrimaryKey
    @NonNull
    private String id;
    private String plateNumber;
    private String vehicleType;
    private String manufacturer;
    private String model;
    private String colour;
    private boolean isAutoPayEnabled;

    public Vehicle() {}

    public Vehicle(@NonNull String id, String plateNumber, String vehicleType, String manufacturer, String model, String colour) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.manufacturer = manufacturer;
        this.model = model;
        this.colour = colour;
        this.isAutoPayEnabled = true;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }
    public boolean isAutoPayEnabled() { return isAutoPayEnabled; }
    public void setAutoPayEnabled(boolean autoPayEnabled) { isAutoPayEnabled = autoPayEnabled; }
}
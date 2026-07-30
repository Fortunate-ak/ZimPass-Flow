package com.zimpassflow.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey
    @NonNull
    private String id;
    private double amount;
    private Date timestamp;
    private String tollgateName;
    private String vehiclePlate;
    private String status;

    public Transaction() {}

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getTollgateName() { return tollgateName; }
    public void setTollgateName(String tollgateName) { this.tollgateName = tollgateName; }
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
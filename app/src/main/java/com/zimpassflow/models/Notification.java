package com.zimpassflow.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String message;
    private Date timestamp;
    private String type; // e.g., "TOLL_PAID", "LOW_BALANCE"

    public Notification(@NonNull String id, String title, String message, Date timestamp, String type) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
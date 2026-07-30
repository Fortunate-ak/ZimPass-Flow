package com.zimpassflow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.zimpassflow.models.Notification;
import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotifications(List<Notification> notifications);

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    LiveData<List<Notification>> getAllNotifications();

    @Query("DELETE FROM notifications")
    void deleteAll();
}
package com.zimpassflow.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.models.Transaction;
import com.zimpassflow.models.Notification;

@Database(entities = {Vehicle.class, Transaction.class, Notification.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract VehicleDao vehicleDao();
    public abstract TransactionDao transactionDao();
    public abstract NotificationDao notificationDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "zimpass_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
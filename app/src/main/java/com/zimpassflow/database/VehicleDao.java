package com.zimpassflow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.zimpassflow.models.Vehicle;
import java.util.List;

@Dao
public interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVehicles(List<Vehicle> vehicles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVehicle(Vehicle vehicle);

    @Update
    void updateVehicle(Vehicle vehicle);

    @Delete
    void deleteVehicle(Vehicle vehicle);

    @Query("SELECT * FROM vehicles")
    LiveData<List<Vehicle>> getAllVehicles();

    @Query("DELETE FROM vehicles")
    void deleteAll();
}
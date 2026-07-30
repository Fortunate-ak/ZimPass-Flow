package com.zimpassflow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.zimpassflow.models.Transaction;
import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactions(List<Transaction> transactions);

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("DELETE FROM transactions")
    void deleteAll();
}
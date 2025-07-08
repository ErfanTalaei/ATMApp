package org.bihe.finalproject.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.bihe.finalproject.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    long insert(Transaction transaction);

//    @Query("DELETE FROM transactions WHERE id = :id")
//    int delete(long id);

    @Delete
    int delete(Transaction transaction);

    @Query("SELECT * FROM transactions")
    List<Transaction> getAll();

    @Query("SELECT * FROM transactions WHERE userName = :userName")
    List<Transaction> getAllByUserName(String userName);
}

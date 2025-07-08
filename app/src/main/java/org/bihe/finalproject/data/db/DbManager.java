package org.bihe.finalproject.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.bihe.finalproject.data.db.dao.TransactionDao;
import org.bihe.finalproject.data.db.dao.UserDao;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.model.User;

@Database(entities = {User.class, Transaction.class}, version = 6)
@TypeConverters({RoomTypeConverters.class})
public abstract class DbManager extends RoomDatabase {

    private final static String DB_NAME = "ATM";
    private static DbManager instance;

    public abstract UserDao userDao();

    public abstract TransactionDao transactionDao();


    public static synchronized DbManager getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DbManager.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

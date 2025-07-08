package org.bihe.finalproject.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.bihe.finalproject.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE userName = :userName AND password = :password")
    List<User> getUserBasedOnCredentials(String userName, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUserName(String username);

    @Query("SELECT * FROM users WHERE accountNum = :accountNum")
    User getUserByAccountNum(String accountNum);

    @Query("SELECT * FROM users WHERE phone = :phone")
    User getUserByPhone(String phone);

    @Query("SELECT * FROM users WHERE cardNum = :cardNum")
    User getUserByCardNum(String cardNum);

    @Query("SELECT * FROM users WHERE cvv2 = :cvv2")
    User getUserByCvv2(String cvv2);

    @Update
    int update(User user);

}

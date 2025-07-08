package org.bihe.finalproject.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "users")
public class User implements Comparable<User> {

    @NonNull
    @PrimaryKey
    @SerializedName("objectId")
    private String id;
    private String username;
    private String password;
    private int age;
    private String phone;
    private String accountNum;
    private String cardNum;
    private String cvv2;
    private String expirationDate;
    private float balance;

    @ColumnInfo
    private String sessionToken;

    @Ignore
    public static List<User> users = new ArrayList<>();

    public User(String id, String username, String password, int age, String phone, String accountNum, String cardNum, String cvv2, String expirationDate, float balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.accountNum = accountNum;
        this.cardNum = cardNum;
        this.cvv2 = cvv2;
        this.expirationDate = expirationDate;
        this.balance = balance;
    }

    @Ignore
    public User(String username, String password, int age, String phone, String accountNum, String cardNum, String cvv2, String expirationDate, float balance) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.accountNum = accountNum;
        this.cardNum = cardNum;
        this.cvv2 = cvv2;
        this.expirationDate = expirationDate;
        this.balance = balance;
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @SuppressLint("SimpleDateFormat")
    public String getFormattedModifiedDate() {
        return new SimpleDateFormat("yy/MM/dd").format(expirationDate);
    }

    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    public int compareTo(User o) {
        return o.expirationDate.compareTo(this.expirationDate);
    }

    public static void insert(User user) {
        users.add(user);
    }

}

package org.bihe.finalproject.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.bihe.finalproject.R;
import org.bihe.finalproject.utils.TypeConstants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "transactions")
public class Transaction implements Serializable, Comparable<Transaction> {

    @NonNull
    @PrimaryKey
    @SerializedName("objectId")
    private String id;
    private String userName;
    private String type;
    private float amount;
    private String originAccountNum;
    private String destinationCardNum;
    private String cvv2;
    private Date modifiedDate;
    private String expirationDate;

    @Ignore
    public static List<Transaction> transactions = new ArrayList<>();

    @Ignore
    public Transaction(String userName, String type, float amount, String originAccountNum, String destinationCardNum, String cvv2, String expirationDate) {
        this.userName = userName;
        this.type = type;
        this.amount = amount;
        this.originAccountNum = originAccountNum;
        this.destinationCardNum = destinationCardNum;
        this.cvv2 = cvv2;
        this.modifiedDate = getDate();
        this.expirationDate = expirationDate;
    }

    @Ignore
    public Transaction(String userName, String type, float amount) {
        this.userName = userName;
        this.type = type;
        this.amount = amount;
        this.modifiedDate = getDate();
    }

    public Transaction(@NonNull String id, String userName, String type, float amount, String originAccountNum, String destinationCardNum, String cvv2, String expirationDate) {
        this.id = id;
        this.userName = userName;
        this.type = type;
        this.amount = amount;
        this.originAccountNum = originAccountNum;
        this.destinationCardNum = destinationCardNum;
        this.cvv2 = cvv2;
        this.modifiedDate = getDate();
        this.expirationDate = expirationDate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getOriginAccountNum() {
        return originAccountNum;
    }

    public void setOriginAccountNum(String originAccountNum) {
        this.originAccountNum = originAccountNum;
    }

    public String getDestinationCardNum() {
        return destinationCardNum;
    }

    public void setDestinationCardNum(String destinationCardNum) {
        this.destinationCardNum = destinationCardNum;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    public int compareTo(Transaction o) {
        return o.modifiedDate.compareTo(this.modifiedDate);
    }

    @SuppressLint("SimpleDateFormat")
    public String getFormattedModifiedDate() {
        return new SimpleDateFormat("yy/MM/dd").format(modifiedDate);
    }

    public int getTypeColor() {
        switch (type) {
            case TypeConstants.CTC:
                return R.color.indigo_500;
            case TypeConstants.WITHDRAW:
                return R.color.blue_500;
        }
        return 0;
    }

    public static void insert(Transaction transaction) {
        transactions.add(0, transaction);
    }


    @NonNull
    @Override
    public String toString() {
        return "Transaction" + '\n' +
                "{ userName= " + userName + '\n' +
                " type= " + type + '\n' +
                "Account number= " + originAccountNum + '\n' +
                "Destination card number= " + destinationCardNum + '\n' +
                " amount= " + amount + '\n' +
                " Modified Date= " + modifiedDate + '\n' +
                "}";
    }

    public String toStringWithdraw() {
        return "Transaction" + '\n' +
                "{ userName= " + userName + '\n' +
                " type= " + type + '\n' +
                " amount= " + amount + '\n' +
                " Modified Date= " + modifiedDate + '\n' +
                "}";
    }
}

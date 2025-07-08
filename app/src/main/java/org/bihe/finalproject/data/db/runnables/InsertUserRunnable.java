package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.utils.ResultListener;

public class InsertUserRunnable implements Runnable {

    private final Context context;
    private final User user;
    private final ResultListener<User> resultListener;

    public InsertUserRunnable(Context context, User user, ResultListener<User> resultListener) {
        this.context = context;
        this.user = user;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        DbManager dbManager = DbManager.getInstance(context);
        User existedUserName = dbManager.userDao().getUserByUserName(user.getUsername());
        User existedAccNum = dbManager.userDao().getUserByUserName(user.getAccountNum());
        User existedPhone = dbManager.userDao().getUserByUserName(user.getPhone());
        User existedCardNum = dbManager.userDao().getUserByUserName(user.getCardNum());
        User existedCvv2 = dbManager.userDao().getUserByUserName(user.getCvv2());


        if (existedUserName != null) {
            resultListener.onError(new Error("Username is not unique"));
            return;
        }

        if (existedAccNum != null) {
            resultListener.onError(new Error("Account number is not unique"));
            return;
        }
        if (existedPhone != null) {
            resultListener.onError(new Error("Phone number is not unique"));
            return;
        }

        if (existedCardNum != null) {
            resultListener.onError(new Error("Card number is not unique"));
            return;
        }

        if (existedCvv2 != null) {
            resultListener.onError(new Error("Cvv2 is not unique"));
            return;
        }

        long id = dbManager.userDao().insert(user);

        if (id > 0) {
            resultListener.onSuccess(user);
        } else {
            resultListener.onError(new Error("User insert Failed"));
        }
    }

}

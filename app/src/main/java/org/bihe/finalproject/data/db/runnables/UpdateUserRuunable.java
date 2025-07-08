package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.utils.ResultListener;

public class UpdateUserRuunable implements Runnable {

    private final Context context;
    private final User user;
    private final float balance;
    private final ResultListener<User> resultListener;

    public UpdateUserRuunable(Context context, User user, float balance, ResultListener<User> resultListener) {
        this.context = context;
        this.user = user;
        this.balance = balance;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        user.setBalance(balance);
        DbManager dbManager = DbManager.getInstance(context);
        long count = dbManager.userDao().update(user);

        if (count > 0) {
            resultListener.onSuccess(user);
        } else {
            resultListener.onError(new Error("Update Failed"));
        }
    }
}

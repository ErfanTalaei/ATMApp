package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.utils.ResultListener;

import java.util.List;

public class GetUserRunnable implements Runnable {
    private final Context context;
    private final String userName;
    private final String password;
    private final ResultListener<User> resultListener;

    public GetUserRunnable(Context context, String userName, String password, ResultListener<User> resultListener) {
        this.context = context;
        this.userName = userName;
        this.password = password;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        DbManager dbManager = DbManager.getInstance(context);
        List<User> users = dbManager.userDao().getUserBasedOnCredentials(userName, password);

        if (users == null || users.isEmpty()) {
            resultListener.onError(new Error("User Not Found"));
        } else {
            resultListener.onSuccess(users.get(0));
        }
    }
}

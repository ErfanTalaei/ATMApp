package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.utils.ResultListener;

import java.util.List;

public class GetTransactionsByUserNameRunnable implements Runnable {

    private final Context context;
    private String userName;
    private final ResultListener<List<Transaction>> resultListener;

    public GetTransactionsByUserNameRunnable(Context context, String userName, ResultListener<List<Transaction>> resultListener) {
        this.context = context;
        this.userName = userName;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        DbManager dbManager = DbManager.getInstance(context);
        List<Transaction> transactions = dbManager.transactionDao().getAllByUserName(userName);

        if (transactions == null) {
            resultListener.onError(new Error("Could not show transactions!"));
        } else {
            resultListener.onSuccess(transactions);
        }
    }
}

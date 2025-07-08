package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.utils.ResultListener;

import java.util.List;

public class GetAllTransactionsRunnable implements Runnable {

    private final Context context;
    private final ResultListener<List<Transaction>> resultListener;

    public GetAllTransactionsRunnable(Context context, ResultListener<List<Transaction>> resultListener) {
        this.context = context;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        DbManager dbManager = DbManager.getInstance(context);
        List<Transaction> transactions = dbManager.transactionDao().getAll();
        if (transactions == null) {
            Error error = new Error("Could not show the transactions!");
            resultListener.onError(error);
        } else {
            resultListener.onSuccess(transactions);
        }
    }
}


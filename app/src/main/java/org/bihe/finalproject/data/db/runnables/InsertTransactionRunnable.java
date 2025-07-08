package org.bihe.finalproject.data.db.runnables;

import android.content.Context;

import org.bihe.finalproject.data.db.DbManager;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.utils.ResultListener;

public class InsertTransactionRunnable implements Runnable {

    private final Context context;
    private final Transaction transaction;
    private final ResultListener<Transaction> resultListener;

    public InsertTransactionRunnable(Context context, Transaction transaction, ResultListener<Transaction> resultListener) {
        this.context = context;
        this.transaction = transaction;
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        DbManager dbManager = DbManager.getInstance(context);
        long id = dbManager.transactionDao().insert(transaction);

        if (id > 0) {
            resultListener.onSuccess(transaction);
        } else {
            resultListener.onError(new Error("Could not do the transaction!"));
        }
    }
}

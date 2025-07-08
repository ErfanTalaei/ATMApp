package org.bihe.finalproject.network.impl;

import androidx.annotation.NonNull;

import org.bihe.finalproject.R;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.model.error.ServerError;
import org.bihe.finalproject.model.response.TransactionResponse;
import org.bihe.finalproject.network.NetworkHelper;
import org.bihe.finalproject.network.service.TransactionService;
import org.bihe.finalproject.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionServiceImpl {

    private final NetworkHelper networkHelper;
    private final TransactionService transactionService;

    public TransactionServiceImpl() {
        networkHelper = new NetworkHelper();
        Retrofit retrofit = networkHelper.buildRetrofit(
                networkHelper.addLoggingInterceptor(networkHelper.getBaseClient()));
        this.transactionService = retrofit.create(TransactionService.class);
    }

    public void deleteTransaction(Transaction transaction, ResultListener<Transaction> resultListener) {
        if (!networkHelper.isNetworkConnected()) {
            networkHelper.showNetworkError(resultListener, R.string.network_connection_error);
            return;
        }
        Call<Transaction> call = transactionService.deleteTransaction(transaction.getId());
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(@NonNull Call<Transaction> call, @NonNull Response<Transaction> response) {
                if (response.isSuccessful()) {
                    resultListener.onSuccess(transaction);
                    return;
                }
                ServerError serverError = networkHelper.convertResponseToError(response, ServerError.class);
                if (serverError == null) {
                    networkHelper.showNetworkError(resultListener, R.string.network_json_error);
                    return;
                }
                resultListener.onError(new Error(serverError.getError()));
            }

            @Override
            public void onFailure(@NonNull Call<Transaction> call, @NonNull Throwable throwable) {
                networkHelper.showNetworkError(resultListener, R.string.network_general_error);
            }
        });
    }

    public void insertTransaction(Transaction transaction, ResultListener<Transaction> resultListener) {
        if (!networkHelper.isNetworkConnected()) {
            networkHelper.showNetworkError(resultListener, R.string.network_connection_error);
            return;
        }
        Call<Transaction> call = transactionService.insertTransaction(transaction);
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(@NonNull Call<Transaction> call, @NonNull Response<Transaction> response) {
                if (response.isSuccessful()) {
                    Transaction received = response.body();
                    if (received == null) {
                        networkHelper.showNetworkError(resultListener, R.string.network_general_error);
                        return;
                    }
                    transaction.setId(received.getId());
                    resultListener.onSuccess(transaction);
                    return;
                }
                ServerError serverError = networkHelper.convertResponseToError(response, ServerError.class);
                if (serverError == null) {
                    networkHelper.showNetworkError(resultListener, R.string.network_json_error);
                    return;
                }
                resultListener.onError(new Error(serverError.getError()));
            }

            @Override
            public void onFailure(@NonNull Call<Transaction> call, @NonNull Throwable throwable) {
                networkHelper.showNetworkError(resultListener, R.string.network_general_error);
            }
        });
    }
}

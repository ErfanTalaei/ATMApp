package org.bihe.finalproject.network.service;

import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.model.response.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TransactionService {
    @DELETE("/classes/transaction/{id}")
    Call<Transaction> deleteTransaction(@Path("id") String id);

    @GET("/classes/transaction?order=-updatedAt")
    Call<TransactionResponse> getTransactions();

    @POST("/classes/transaction")
    Call<Transaction> insertTransaction(@Body Transaction transaction);
}

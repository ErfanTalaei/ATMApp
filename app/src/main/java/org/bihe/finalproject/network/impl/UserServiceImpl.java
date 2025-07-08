package org.bihe.finalproject.network.impl;

import androidx.annotation.NonNull;

import org.bihe.finalproject.R;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.model.error.ServerError;
import org.bihe.finalproject.network.NetworkHelper;
import org.bihe.finalproject.network.service.UserService;
import org.bihe.finalproject.utils.ResultListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserServiceImpl {
    private final NetworkHelper networkHelper;
    private final UserService userService;

    public UserServiceImpl() {
        networkHelper = new NetworkHelper();
        Retrofit retrofit = networkHelper.buildRetrofit(
                networkHelper.addLoggingInterceptor(networkHelper.getUserClient()));
        this.userService = retrofit.create(UserService.class);
    }

    public void signUpUser(User user, ResultListener<User> resultListener) {
        if (!networkHelper.isNetworkConnected()) {
            networkHelper.showNetworkError(resultListener, R.string.network_connection_error);
            return;
        }
        Call<User> call = userService.signUpUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User received = response.body();
                    if (received == null) {
                        networkHelper.showNetworkError(resultListener, R.string.network_general_error);
                        return;
                    }
                    received.setUsername(user.getUsername());
                    received.setPassword(user.getPassword());
                    received.setAge(user.getAge());
                    received.setPhone(user.getPhone());
                    received.setAccountNum(user.getAccountNum());
                    received.setCardNum(user.getCardNum());
                    received.setCvv2(user.getCvv2());
                    received.setExpirationDate(user.getExpirationDate());
                    received.setBalance(user.getBalance());
                    resultListener.onSuccess(received);
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                networkHelper.showNetworkError(resultListener, R.string.network_general_error);
            }
        });
    }

    public void loginUser(String username, String password, ResultListener<User> resultListener) {
        if (!networkHelper.isNetworkConnected()) {
            networkHelper.showNetworkError(resultListener, R.string.network_connection_error);
            return;
        }
        Call<User> call = userService.loginUser(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User received = response.body();
                    if (received == null) {
                        networkHelper.showNetworkError(resultListener, R.string.network_general_error);
                        return;
                    }
                    received.setUsername(username);
                    received.setPassword(password);
                    resultListener.onSuccess(received);
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                networkHelper.showNetworkError(resultListener, R.string.network_general_error);
            }
        });
    }
}

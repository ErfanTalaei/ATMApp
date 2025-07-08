package org.bihe.finalproject.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.bihe.finalproject.data.AppData;
import org.bihe.finalproject.network.interceptor.BaseInterceptor;
import org.bihe.finalproject.network.interceptor.UserInterceptor;
import org.bihe.finalproject.utils.NetworkConstants;
import org.bihe.finalproject.utils.ResultListener;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {

    public Builder getBaseClient() {
        return new OkHttpClient.Builder().addInterceptor(new BaseInterceptor());
    }

    public Builder getUserClient() {
        return getBaseClient().addInterceptor(new UserInterceptor());
    }

    public Builder addLoggingInterceptor(@NonNull Builder builder) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        return builder.addInterceptor(loggingInterceptor);
    }

    public Retrofit buildRetrofit(@NonNull Builder builder) {
        return buildRetrofit(builder.build());
    }

    public Retrofit buildRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstants.BASE_URL)
                .build();
    }

    public <T, U> U convertResponseToError(retrofit2.Response<T> response, Class<U> className) {
        try (ResponseBody responseBody = response.errorBody()) {
            if (responseBody == null) {
                return null;
            }
            String responseStr = responseBody.string();
            return new Gson().fromJson(responseStr, className);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isNetworkConnected() {
        Context context = AppData.getInstance().getApplicationContext();
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public <T> void showNetworkError(@NonNull ResultListener<T> resultListener, int resourceId) {
        Context context = AppData.getInstance().getApplicationContext();
        String error = context.getString(resourceId);
        resultListener.onError(new Error(error));
    }

}

package org.bihe.finalproject.network.interceptor;

import androidx.annotation.NonNull;

import org.bihe.finalproject.utils.NetworkConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request neRequest = original.newBuilder()
                .header(NetworkConstants.APPLICATION_ID_KEY, NetworkConstants.APPLICATION_ID_VALUE)
                .header(NetworkConstants.REST_API_KEY, NetworkConstants.REST_API_VALUE)
                .build();
        return chain.proceed(neRequest);
    }
}

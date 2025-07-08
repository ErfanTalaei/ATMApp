package org.bihe.finalproject.network.interceptor;

import androidx.annotation.NonNull;

import org.bihe.finalproject.utils.NetworkConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request neRequest = original.newBuilder()
                .header(NetworkConstants.REVOCABLE_SESSION_KEY, NetworkConstants.REVOCABLE_SESSION_VALUE)
                .build();
        return chain.proceed(neRequest);
    }
}

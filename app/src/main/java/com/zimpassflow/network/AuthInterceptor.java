package com.zimpassflow.network;

import com.zimpassflow.utils.PreferenceManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private PreferenceManager preferenceManager;

    public AuthInterceptor(PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = preferenceManager.getToken();
        Request.Builder requestBuilder = chain.request().newBuilder();

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(requestBuilder.build());
    }
}
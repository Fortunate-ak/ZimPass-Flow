package com.zimpassflow.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zimpassflow.api.ApiService;
import com.zimpassflow.models.AuthResponse;
import com.zimpassflow.models.LoginRequest;
import com.zimpassflow.models.RegisterRequest;
import com.zimpassflow.network.RetrofitClient;
import com.zimpassflow.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository(android.content.Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public LiveData<Resource<AuthResponse>> login(LoginRequest loginRequest) {
        MutableLiveData<Resource<AuthResponse>> loginData = new MutableLiveData<>();
        loginData.setValue(Resource.loading(null));

        apiService.login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginData.setValue(Resource.success(response.body()));
                } else {
                    loginData.setValue(Resource.error("Invalid credentials", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginData.setValue(Resource.error("Network failure: " + t.getMessage(), null));
            }
        });
        return loginData;
    }

    public LiveData<Resource<AuthResponse>> register(RegisterRequest registerRequest) {
        MutableLiveData<Resource<AuthResponse>> registerData = new MutableLiveData<>();
        registerData.setValue(Resource.loading(null));

        apiService.register(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerData.setValue(Resource.success(response.body()));
                } else {
                    registerData.setValue(Resource.error("Registration failed", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                registerData.setValue(Resource.error("Network failure: " + t.getMessage(), null));
            }
        });
        return registerData;
    }

    public LiveData<Resource<Void>> forgotPassword(String email) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.forgotPassword(email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(null));
                } else {
                    result.setValue(Resource.error("Failed to send reset link", null));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return result;
    }
}
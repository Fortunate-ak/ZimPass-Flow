package com.zimpassflow.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zimpassflow.models.AuthResponse;
import com.zimpassflow.models.LoginRequest;
import com.zimpassflow.models.RegisterRequest;
import com.zimpassflow.repositories.AuthRepository;
import com.zimpassflow.utils.Resource;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Resource<AuthResponse>> login(String email, String password) {
        return authRepository.login(new LoginRequest(email, password));
    }

    public LiveData<Resource<AuthResponse>> register(RegisterRequest registerRequest) {
        return authRepository.register(registerRequest);
    }

    public LiveData<Resource<Void>> forgotPassword(String email) {
        return authRepository.forgotPassword(email);
    }
}
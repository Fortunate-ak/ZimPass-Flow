package com.zimpassflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.zimpassflow.databinding.ActivityRegisterBinding;
import com.zimpassflow.models.RegisterRequest;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        preferenceManager = new PreferenceManager(this);

        binding.btnRegister.setOnClickListener(v -> registerUser());

        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String fullName = binding.etFullName.getText().toString().trim();
        String nationalId = binding.etNationalId.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (fullName.isEmpty() || nationalId.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(fullName, nationalId, phoneNumber, email, password);

        authViewModel.register(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        // Show loading state
                        binding.btnRegister.setEnabled(false);
                        break;
                    case SUCCESS:
                        binding.btnRegister.setEnabled(true);
                        if (resource.data != null) {
                            preferenceManager.saveToken(resource.data.getToken());
                            Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case ERROR:
                        binding.btnRegister.setEnabled(true);
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
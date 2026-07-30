package com.zimpassflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.zimpassflow.databinding.ActivityLoginBinding;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        preferenceManager = new PreferenceManager(this);

        binding.btnLogin.setOnClickListener(v -> loginUser());

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        binding.btnForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void loginUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.login(email, password).observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.btnLogin.setEnabled(false);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnLogin.setEnabled(true);
                        if (resource.data != null) {
                            preferenceManager.saveToken(resource.data.getToken());
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnLogin.setEnabled(true);
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
package com.zimpassflow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.zimpassflow.databinding.ActivityForgotPasswordBinding;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnSubmit.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                sendResetLink(email);
            }
        });

        binding.btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void sendResetLink(String email) {
        viewModel.forgotPassword(email).observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        binding.btnSubmit.setEnabled(false);
                        break;
                    case SUCCESS:
                        binding.btnSubmit.setEnabled(true);
                        Toast.makeText(this, "Reset link sent to " + email, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case ERROR:
                        binding.btnSubmit.setEnabled(true);
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
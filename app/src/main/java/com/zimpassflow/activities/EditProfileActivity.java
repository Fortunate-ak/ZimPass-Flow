package com.zimpassflow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.zimpassflow.databinding.ActivityEditProfileBinding;
import com.zimpassflow.models.User;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.viewmodels.MainViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(this);

        setupToolbar();
        observeUserData();

        binding.btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void observeUserData() {
        viewModel.getDashboardData().observe(this, data -> {
            if (data != null && data.getUser() != null) {
                User user = data.getUser();
                binding.etFullName.setText(user.getFullName());
                binding.etEmail.setText(user.getEmail());
                binding.etPhone.setText(user.getPhoneNumber());
                binding.etNationalId.setText(user.getNationalId());
            }
        });
        
        // Fetch data if not already available
        viewModel.fetchDashboardData(preferenceManager.getToken());
    }

    private void updateProfile() {
        String name = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        updatedUser.setFullName(name);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phone);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnUpdate.setEnabled(false);
        
        viewModel.updateProfile(preferenceManager.getToken(), updatedUser).observe(this, success -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnUpdate.setEnabled(true);
            if (success) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.zimpassflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.zimpassflow.R;
import com.zimpassflow.activities.ChangePasswordActivity;
import com.zimpassflow.activities.EditProfileActivity;
import com.zimpassflow.activities.LoginActivity;
import com.zimpassflow.databinding.FragmentProfileBinding;
import com.zimpassflow.utils.PreferenceManager;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(requireContext());

        // Display user info (In real app, observe from ViewModel)
        binding.tvUserName.setText("John Doe");
        binding.tvUserEmail.setText("john.doe@example.com");

        binding.btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), EditProfileActivity.class));
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });

        binding.btnSettings.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_settings));

        binding.btnHelp.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Support ticket system coming soon", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        preferenceManager.clear();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
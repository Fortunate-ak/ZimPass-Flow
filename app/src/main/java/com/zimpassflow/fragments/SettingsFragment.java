package com.zimpassflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zimpassflow.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Dark Mode Toggle
        binding.switchDarkMode.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Language Selection
        binding.llLanguage.setOnClickListener(v -> showLanguageDialog());

        // Information Links
        binding.btnAbout.setOnClickListener(v -> showInfoDialog("About ZimPass Flow", "ZimPass Flow is a smart toll payment system designed for vehicle owners in Zimbabwe. Version 1.0.0"));
        binding.btnPrivacy.setOnClickListener(v -> showInfoDialog("Privacy Policy", "Your data is stored securely. We only share information required for toll processing with authorized authorities."));
        binding.btnHelp.setOnClickListener(v -> Toast.makeText(getContext(), "Redirecting to help center...", Toast.LENGTH_SHORT).show());
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Shona", "Ndebele"};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    Toast.makeText(getContext(), "Language changed to " + languages[which], Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showInfoDialog(String title, String message) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
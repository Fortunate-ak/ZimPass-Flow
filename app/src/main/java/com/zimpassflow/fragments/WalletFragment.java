package com.zimpassflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zimpassflow.adapters.TransactionAdapter;
import com.zimpassflow.databinding.DialogTopUpBinding;
import com.zimpassflow.databinding.FragmentWalletBinding;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.MainViewModel;

public class WalletFragment extends Fragment {
    private FragmentWalletBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private TransactionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(requireContext());

        setupRecyclerView();
        loadWalletData();

        binding.btnTopUp.setOnClickListener(v -> showTopUpDialog());
    }

    private void setupRecyclerView() {
        adapter = new TransactionAdapter();
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvHistory.setAdapter(adapter);
    }

    private void loadWalletData() {
        String token = preferenceManager.getToken();
        if (token == null) return;

        viewModel.fetchDashboardData(token).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        // Show progress if needed
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            binding.tvWalletBalance.setText(String.format("$%.2f", resource.data.getWalletBalance()));
                            if (resource.data.getRecentTransactions() != null) {
                                adapter.setTransactions(resource.data.getRecentTransactions());
                            }
                        }
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showTopUpDialog() {
        DialogTopUpBinding dialogBinding = DialogTopUpBinding.inflate(getLayoutInflater());
        
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Top Up Wallet")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Top Up", (dialog, which) -> {
                    String amountStr = dialogBinding.etAmount.getText().toString();
                    if (!amountStr.isEmpty()) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            if (amount > 0) {
                                performTopUp(amount);
                            } else {
                                Toast.makeText(getContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performTopUp(double amount) {
        viewModel.topUpWallet(preferenceManager.getToken(), amount).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        // Show progress
                        break;
                    case SUCCESS:
                        Toast.makeText(getContext(), "Top up successful!", Toast.LENGTH_SHORT).show();
                        loadWalletData(); // Refresh balance
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Top up failed: " + resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
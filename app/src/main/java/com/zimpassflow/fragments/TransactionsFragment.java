package com.zimpassflow.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zimpassflow.adapters.TransactionAdapter;
import com.zimpassflow.databinding.FragmentTransactionsBinding;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.MainViewModel;
import com.google.android.material.chip.Chip;

public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private TransactionAdapter adapter;
    private String currentStatus = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(requireContext());

        setupRecyclerView();
        setupFilters();
        setupSearch();

        loadTransactions();
    }

    private void setupRecyclerView() {
        adapter = new TransactionAdapter();
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTransactions.setAdapter(adapter);
    }

    private void setupFilters() {
        binding.cgFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                currentStatus = chip.getText().toString();
                loadTransactions();
            }
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadTransactions();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadTransactions() {
        String token = preferenceManager.getToken();
        if (token == null) return;

        String query = binding.etSearch.getText().toString().trim();
        String status = currentStatus.equals("All") ? "" : currentStatus.toLowerCase();

        viewModel.searchTransactions(token, query, status).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        if (resource.data != null) {
                            adapter.setTransactions(resource.data);
                            updateUI(resource.data.isEmpty());
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateUI(boolean isEmpty) {
        binding.rvTransactions.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
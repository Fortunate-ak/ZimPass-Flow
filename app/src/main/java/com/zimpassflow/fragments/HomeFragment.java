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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zimpassflow.R;
import com.zimpassflow.adapters.DashboardVehicleAdapter;
import com.zimpassflow.adapters.TransactionAdapter;
import com.zimpassflow.databinding.FragmentHomeBinding;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.MainViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private TransactionAdapter transactionAdapter;
    private DashboardVehicleAdapter vehicleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(requireContext());

        setupRecyclerViews();
        setupQuickActions();
        loadDashboardData();
    }

    private void setupRecyclerViews() {
        transactionAdapter = new TransactionAdapter();
        binding.rvRecentTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRecentTransactions.setAdapter(transactionAdapter);

        vehicleAdapter = new DashboardVehicleAdapter();
        binding.rvDashboardVehicles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvDashboardVehicles.setAdapter(vehicleAdapter);
    }

    private void setupQuickActions() {
        binding.llAddVehicle.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_vehicles));
        
        binding.llWalletAction.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_wallet));
        
        binding.llTransactionsAction.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_transactions));

        binding.llNotificationsAction.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_notifications));
            
        binding.btnTopUp.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_wallet));
    }

    private void loadDashboardData() {
        String token = preferenceManager.getToken();
        if (token == null) return;

        viewModel.fetchDashboardData(token).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        // Show global loading if needed
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            binding.tvWelcome.setText(resource.data.getWelcomeMessage());
                            binding.tvBalance.setText(String.format("$%.2f", resource.data.getWalletBalance()));
                            
                            if (resource.data.getWalletBalance() < 10.0) {
                                binding.cvWarning.setVisibility(View.VISIBLE);
                            } else {
                                binding.cvWarning.setVisibility(View.GONE);
                            }

                            if (resource.data.getRecentTransactions() != null && !resource.data.getRecentTransactions().isEmpty()) {
                                transactionAdapter.setTransactions(resource.data.getRecentTransactions());
                                binding.rvRecentTransactions.setVisibility(View.VISIBLE);
                                binding.tvNoTransactions.setVisibility(View.GONE);
                            } else {
                                binding.rvRecentTransactions.setVisibility(View.GONE);
                                binding.tvNoTransactions.setVisibility(View.VISIBLE);
                            }

                            if (resource.data.getVehicles() != null && !resource.data.getVehicles().isEmpty()) {
                                vehicleAdapter.setVehicles(resource.data.getVehicles());
                                binding.rvDashboardVehicles.setVisibility(View.VISIBLE);
                                binding.tvVehiclesLabel.setVisibility(View.VISIBLE);
                            } else {
                                binding.rvDashboardVehicles.setVisibility(View.GONE);
                                binding.tvVehiclesLabel.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
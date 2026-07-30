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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zimpassflow.activities.AddVehicleActivity;
import com.zimpassflow.adapters.VehicleAdapter;
import com.zimpassflow.databinding.FragmentVehiclesBinding;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.List;

public class VehiclesFragment extends Fragment implements VehicleAdapter.OnVehicleClickListener {
    private FragmentVehiclesBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private VehicleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVehiclesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(requireContext());

        setupRecyclerView();
        loadVehicles();

        binding.fabAddVehicle.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddVehicleActivity.class));
        });
    }

    private void setupRecyclerView() {
        adapter = new VehicleAdapter();
        adapter.setOnVehicleClickListener(this);
        binding.rvVehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvVehicles.setAdapter(adapter);
    }

    private void loadVehicles() {
        String token = preferenceManager.getToken();
        if (token == null) return;

        viewModel.fetchVehicles(token).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        if (resource.data != null) {
                            adapter.setVehicles(resource.data);
                            updateEmptyState(resource.data.isEmpty());
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Local cache fallback
        viewModel.getLocalVehicles().observe(getViewLifecycleOwner(), vehicles -> {
            if (vehicles != null && adapter.getItemCount() == 0) {
                adapter.setVehicles(vehicles);
                updateEmptyState(vehicles.isEmpty());
            }
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.rvVehicles.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.tvNoVehicles.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onVehicleEdit(Vehicle vehicle) {
        Intent intent = new Intent(requireContext(), AddVehicleActivity.class);
        intent.putExtra(AddVehicleActivity.EXTRA_VEHICLE, new Gson().toJson(vehicle));
        startActivity(intent);
    }

    @Override
    public void onVehicleDelete(Vehicle vehicle) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Vehicle")
                .setMessage("Are you sure you want to remove " + vehicle.getPlateNumber() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteVehicle(preferenceManager.getToken(), vehicle.getId())
                            .observe(getViewLifecycleOwner(), resource -> {
                                if (resource != null && resource.status == Resource.Status.SUCCESS) {
                                    Toast.makeText(getContext(), "Vehicle deleted", Toast.LENGTH_SHORT).show();
                                    loadVehicles(); // Refresh list
                                } else if (resource != null && resource.status == Resource.Status.ERROR) {
                                    Toast.makeText(getContext(), "Failed to delete: " + resource.message, Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
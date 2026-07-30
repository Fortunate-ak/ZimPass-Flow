package com.zimpassflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zimpassflow.adapters.NotificationAdapter;
import com.zimpassflow.databinding.FragmentNotificationsBinding;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.viewmodels.MainViewModel;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(requireContext());

        setupRecyclerView();
        observeViewModel();

        viewModel.fetchNotifications(preferenceManager.getToken());
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter();
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotifications.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getNotifications().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                adapter.setNotifications(list);
                binding.rvNotifications.setVisibility(View.VISIBLE);
                binding.tvNoNotifications.setVisibility(View.GONE);
            } else {
                binding.rvNotifications.setVisibility(View.GONE);
                binding.tvNoNotifications.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Optional: Show a shimmer or progress bar specific to this list
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.zimpassflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zimpassflow.databinding.ItemVehicleBinding;
import com.zimpassflow.models.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicles = new ArrayList<>();
    private OnVehicleClickListener listener;

    public interface OnVehicleClickListener {
        void onVehicleEdit(Vehicle vehicle);
        void onVehicleDelete(Vehicle vehicle);
        void onAutoPayToggle(Vehicle vehicle, boolean isEnabled);
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewHolder(
                ItemVehicleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.bind(vehicles.get(position));
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {
        private ItemVehicleBinding binding;

        public VehicleViewHolder(ItemVehicleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Vehicle vehicle) {
            binding.tvPlateNumber.setText(vehicle.getPlateNumber());
            binding.tvVehicleInfo.setText(String.format("%s • %s • %s", 
                    vehicle.getManufacturer(), vehicle.getModel(), vehicle.getColour()));
            
            binding.chipAutoPay.setText(vehicle.isAutoPayEnabled() ? "Auto-Pay Active" : "Auto-Pay Disabled");
            binding.chipAutoPay.setChipIconResource(vehicle.isAutoPayEnabled() ? 
                    android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background);

            binding.ibDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVehicleDelete(vehicle);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVehicleEdit(vehicle);
                }
            });
            
            binding.chipAutoPay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAutoPayToggle(vehicle, !vehicle.isAutoPayEnabled());
                }
            });
        }
    }
}
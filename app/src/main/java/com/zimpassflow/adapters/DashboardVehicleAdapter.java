package com.zimpassflow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zimpassflow.databinding.ItemDashboardVehicleBinding;
import com.zimpassflow.models.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class DashboardVehicleAdapter extends RecyclerView.Adapter<DashboardVehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicles = new ArrayList<>();

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewHolder(
                ItemDashboardVehicleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
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

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        private ItemDashboardVehicleBinding binding;

        public VehicleViewHolder(ItemDashboardVehicleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Vehicle vehicle) {
            binding.tvPlate.setText(vehicle.getPlateNumber());
            binding.tvModel.setText(vehicle.getManufacturer() + " " + vehicle.getModel());
        }
    }
}
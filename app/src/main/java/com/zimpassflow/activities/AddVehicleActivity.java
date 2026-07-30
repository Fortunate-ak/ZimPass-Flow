package com.zimpassflow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.zimpassflow.databinding.ActivityAddVehicleBinding;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.utils.PreferenceManager;
import com.zimpassflow.utils.Resource;
import com.zimpassflow.viewmodels.MainViewModel;
import com.google.gson.Gson;

public class AddVehicleActivity extends AppCompatActivity {
    public static final String EXTRA_VEHICLE = "extra_vehicle";
    private ActivityAddVehicleBinding binding;
    private MainViewModel viewModel;
    private PreferenceManager preferenceManager;
    private Vehicle existingVehicle;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        preferenceManager = new PreferenceManager(this);

        if (getIntent().hasExtra(EXTRA_VEHICLE)) {
            String vehicleJson = getIntent().getStringExtra(EXTRA_VEHICLE);
            existingVehicle = new Gson().fromJson(vehicleJson, Vehicle.class);
            isEditMode = true;
        }

        setupToolbar();
        setupVehicleTypeSpinner();
        
        if (isEditMode) {
            populateFields();
            binding.btnSave.setText("Update Vehicle");
        }

        binding.btnSave.setOnClickListener(v -> saveVehicle());
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Vehicle" : "Add New Vehicle");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupVehicleTypeSpinner() {
        String[] types = {"Light Vehicle", "Minibus", "Bus", "Heavy Vehicle"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types);
        binding.actvType.setAdapter(adapter);
    }

    private void populateFields() {
        binding.etPlate.setText(existingVehicle.getPlateNumber());
        binding.actvType.setText(existingVehicle.getVehicleType(), false);
        binding.etManufacturer.setText(existingVehicle.getManufacturer());
        binding.etModel.setText(existingVehicle.getModel());
        binding.etColour.setText(existingVehicle.getColour());
    }

    private void saveVehicle() {
        String plate = binding.etPlate.getText().toString().trim();
        String type = binding.actvType.getText().toString().trim();
        String manufacturer = binding.etManufacturer.getText().toString().trim();
        String model = binding.etModel.getText().toString().trim();
        String colour = binding.etColour.getText().toString().trim();

        if (plate.isEmpty() || type.isEmpty() || manufacturer.isEmpty() || model.isEmpty() || colour.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            existingVehicle.setPlateNumber(plate);
            existingVehicle.setVehicleType(type);
            existingVehicle.setManufacturer(manufacturer);
            existingVehicle.setModel(model);
            existingVehicle.setColour(colour);
            
            viewModel.updateVehicle(preferenceManager.getToken(), existingVehicle).observe(this, this::handleResource);
        } else {
            Vehicle vehicle = new Vehicle("", plate, type, manufacturer, model, colour);
            viewModel.addVehicle(preferenceManager.getToken(), vehicle).observe(this, this::handleResource);
        }
    }

    private void handleResource(Resource<Vehicle> resource) {
        if (resource != null) {
            switch (resource.status) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnSave.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(this, isEditMode ? "Vehicle updated" : "Vehicle registered", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
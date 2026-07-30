package com.zimpassflow.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zimpassflow.models.DashboardData;
import com.zimpassflow.models.Notification;
import com.zimpassflow.models.Transaction;
import com.zimpassflow.models.User;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.repositories.MainRepository;
import com.zimpassflow.utils.Resource;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
    }

    public LiveData<Resource<DashboardData>> fetchDashboardData(String token) {
        return repository.getDashboardData(token);
    }

    public LiveData<Resource<List<Vehicle>>> fetchVehicles(String token) {
        return repository.getVehicles(token);
    }

    public LiveData<Resource<Vehicle>> addVehicle(String token, Vehicle vehicle) {
        return repository.addVehicle(token, vehicle);
    }

    public LiveData<Resource<Vehicle>> updateVehicle(String token, Vehicle vehicle) {
        return repository.updateVehicle(token, vehicle);
    }

    public LiveData<Resource<Void>> deleteVehicle(String token, String vehicleId) {
        return repository.deleteVehicle(token, vehicleId);
    }

    public LiveData<Resource<Void>> topUpWallet(String token, double amount) {
        return repository.topUpWallet(token, amount);
    }

    public LiveData<Resource<List<Transaction>>> searchTransactions(String token, String query, String status) {
        return repository.getTransactions(token, query, status);
    }

    public LiveData<Resource<List<Notification>>> fetchNotifications(String token) {
        return repository.getNotifications(token);
    }

    public LiveData<Resource<Void>> updateProfile(String token, User user) {
        return repository.updateProfile(token, user);
    }

    public LiveData<Resource<Void>> changePassword(String token, String newPassword) {
        return repository.changePassword(token, newPassword);
    }

    public LiveData<List<Vehicle>> getLocalVehicles() {
        return repository.getLocalVehicles();
    }

    public LiveData<List<Transaction>> getLocalTransactions() {
        return repository.getLocalTransactions();
    }

    public LiveData<List<Notification>> getLocalNotifications() {
        return repository.getLocalNotifications();
    }
}
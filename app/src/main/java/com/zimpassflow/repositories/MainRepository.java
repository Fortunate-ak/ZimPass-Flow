package com.zimpassflow.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zimpassflow.api.ApiService;
import com.zimpassflow.database.AppDatabase;
import com.zimpassflow.database.NotificationDao;
import com.zimpassflow.database.TransactionDao;
import com.zimpassflow.database.VehicleDao;
import com.zimpassflow.models.DashboardData;
import com.zimpassflow.models.Notification;
import com.zimpassflow.models.TopUpRequest;
import com.zimpassflow.models.Transaction;
import com.zimpassflow.models.User;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.network.RetrofitClient;
import com.zimpassflow.utils.Resource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private final ApiService apiService;
    private final VehicleDao vehicleDao;
    private final TransactionDao transactionDao;
    private final NotificationDao notificationDao;
    private final ExecutorService executorService;

    public MainRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
        AppDatabase db = AppDatabase.getInstance(context);
        this.vehicleDao = db.vehicleDao();
        this.transactionDao = db.transactionDao();
        this.notificationDao = db.notificationDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Resource<DashboardData>> getDashboardData() {
        MutableLiveData<Resource<DashboardData>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.getDashboardData().enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                    executorService.execute(() -> {
                        DashboardData body = response.body();
                        if (body.getVehicles() != null) {
                            vehicleDao.deleteAll();
                            vehicleDao.insertVehicles(body.getVehicles());
                        }
                        if (body.getRecentTransactions() != null) {
                            transactionDao.deleteAll();
                            transactionDao.insertTransactions(body.getRecentTransactions());
                        }
                    });
                } else {
                    data.setValue(Resource.error("Failed to fetch dashboard", null));
                }
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<Vehicle>>> getVehicles() {
        MutableLiveData<Resource<List<Vehicle>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.getVehicles().enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                    executorService.execute(() -> {
                        vehicleDao.deleteAll();
                        vehicleDao.insertVehicles(response.body());
                    });
                } else {
                    data.setValue(Resource.error("Failed to fetch vehicles", null));
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Vehicle>> addVehicle(Vehicle vehicle) {
        MutableLiveData<Resource<Vehicle>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.addVehicle(vehicle).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                    executorService.execute(() -> vehicleDao.insertVehicle(response.body()));
                } else {
                    data.setValue(Resource.error("Failed to add vehicle", null));
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Vehicle>> updateVehicle(Vehicle vehicle) {
        MutableLiveData<Resource<Vehicle>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.updateVehicle(vehicle.getId(), vehicle).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                    executorService.execute(() -> vehicleDao.updateVehicle(response.body()));
                } else {
                    data.setValue(Resource.error("Failed to update vehicle", null));
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Void>> deleteVehicle(String vehicleId) {
        MutableLiveData<Resource<Void>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.deleteVehicle(vehicleId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(null));
                    // Handle local deletion if necessary or refresh
                } else {
                    data.setValue(Resource.error("Failed to delete", null));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Vehicle>> toggleAutoPay(String vehicleId, boolean enabled) {
        MutableLiveData<Resource<Vehicle>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.toggleAutoPay(vehicleId, enabled).enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                    executorService.execute(() -> vehicleDao.updateVehicle(response.body()));
                } else {
                    data.setValue(Resource.error("Failed to toggle Auto-Pay", null));
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Void>> topUpWallet(double amount, String method) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        apiService.topUpWallet(new TopUpRequest(amount, method)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(null));
                } else {
                    result.setValue(Resource.error("Top-up failed", null));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return result;
    }

    public LiveData<Resource<List<Transaction>>> getTransactions(String query, String status) {
        MutableLiveData<Resource<List<Transaction>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.getTransactions(query, status).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error("Failed to fetch history", null));
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<Notification>>> getNotifications() {
        MutableLiveData<Resource<List<Notification>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        apiService.getNotifications().enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error("Failed to fetch alerts", null));
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<List<Vehicle>> getLocalVehicles() { return vehicleDao.getAllVehicles(); }
    public LiveData<List<Transaction>> getLocalTransactions() { return transactionDao.getAllTransactions(); }
}
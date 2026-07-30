package com.zimpassflow.api;

import com.zimpassflow.models.AuthResponse;
import com.zimpassflow.models.DashboardData;
import com.zimpassflow.models.LoginRequest;
import com.zimpassflow.models.Notification;
import com.zimpassflow.models.RegisterRequest;
import com.zimpassflow.models.TopUpRequest;
import com.zimpassflow.models.Transaction;
import com.zimpassflow.models.Vehicle;
import com.zimpassflow.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // --- Authentication (Public) ---
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @POST("auth/forgot-password")
    Call<Void> forgotPassword(@Body String email);

    // --- User & Profile ---
    @POST("auth/change-password")
    Call<Void> changePassword(@Body String newPassword);

    @GET("user/dashboard")
    Call<DashboardData> getDashboardData();

    @PUT("user/profile")
    Call<Void> updateProfile(@Body User user);

    // --- Vehicles ---
    @GET("vehicles")
    Call<List<Vehicle>> getVehicles();

    @POST("vehicles")
    Call<Vehicle> addVehicle(@Body Vehicle vehicle);

    @PUT("vehicles/{id}")
    Call<Vehicle> updateVehicle(@Path("id") String vehicleId, @Body Vehicle vehicle);

    @DELETE("vehicles/{id}")
    Call<Void> deleteVehicle(@Path("id") String vehicleId);

    @PUT("vehicles/{id}/toggle-autopay")
    Call<Vehicle> toggleAutoPay(@Path("id") String vehicleId, @Query("enabled") boolean enabled);

    // --- Wallet & Transactions ---
    @GET("transactions")
    Call<List<Transaction>> getTransactions(
            @Query("search") String query,
            @Query("status") String status
    );

    @POST("wallet/topup")
    Call<Void> topUpWallet(@Body TopUpRequest topUpRequest);

    // --- Notifications ---
    @GET("notifications")
    Call<List<Notification>> getNotifications();
}
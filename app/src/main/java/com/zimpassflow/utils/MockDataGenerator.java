package com.zimpassflow.utils;

import com.zimpassflow.models.DashboardData;
import com.zimpassflow.models.Notification;
import com.zimpassflow.models.Transaction;
import com.zimpassflow.models.Vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockDataGenerator {

    public static DashboardData getMockDashboardData() {
        DashboardData data = new DashboardData();
        data.setWelcomeMessage("Welcome back, Tinashe!");
        data.setWalletBalance(45.50);
        
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("1", "AEE-1234", "Light Vehicle", "Toyota", "Corolla", "Silver"));
        vehicles.add(new Vehicle("2", "AGE-5678", "Light Vehicle", "Mazda", "Axela", "Blue"));
        data.setVehicles(vehicles);
        
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("1", 2.00, "Skyline Tollgate", "AEE-1234", "Completed"));
        transactions.add(createTransaction("2", 2.00, "Norton Tollgate", "AGE-5678", "Completed"));
        transactions.add(createTransaction("3", 10.00, "Wallet Top-up", "N/A", "Completed"));
        data.setRecentTransactions(transactions);
        
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("1", "Toll Paid", "Successfully paid $2.00 at Skyline Tollgate.", new Date(), "TOLL_PAID"));
        notifications.add(new Notification("2", "Low Balance Warning", "Your wallet balance is below $10.00.", new Date(), "LOW_BALANCE"));
        data.setNotifications(notifications);
        
        return data;
    }

    private static Transaction createTransaction(String id, double amount, String toll, String plate, String status) {
        Transaction t = new Transaction();
        t.setId(id);
        t.setAmount(amount);
        t.setTollgateName(toll);
        t.setVehiclePlate(plate);
        t.setStatus(status);
        t.setTimestamp(new Date());
        return t;
    }
}
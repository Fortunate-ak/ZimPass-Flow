package com.zimpassflow.models;

import java.util.List;

public class DashboardData {
    private User user;
    private double walletBalance;
    private List<Vehicle> vehicles;
    private List<Transaction> recentTransactions;
    private List<Notification> notifications;
    private String welcomeMessage;

    public DashboardData() {}

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public double getWalletBalance() { return walletBalance; }
    public void setWalletBalance(double walletBalance) { this.walletBalance = walletBalance; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }
    public List<Transaction> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<Transaction> recentTransactions) { this.recentTransactions = recentTransactions; }
    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }
    public String getWelcomeMessage() { return welcomeMessage; }
    public void setWelcomeMessage(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }
}
package com.zimpassflow.models;

public class TopUpRequest {
    private double amount;
    private String paymentMethod;

    public TopUpRequest(double amount, String paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
}
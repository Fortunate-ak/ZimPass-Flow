package com.zimpassflow.models;

public class RegisterRequest {
    private String fullName;
    private String nationalId;
    private String phoneNumber;
    private String email;
    private String password;

    public RegisterRequest(String fullName, String nationalId, String phoneNumber, String email, String password) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
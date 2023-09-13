package com.example.fooddeliveryapk.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String address;
    private String fcmToken;
    public UserModel(String phoneNumber, String username, String address, Timestamp now, String s) {
    }

    public UserModel(String phone, String username, String address, Timestamp createdTimestamp, String userId, String fcmToken) {
        this.phone = phone;
        this.username = username;
        this.address = address;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.fcmToken = fcmToken;
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
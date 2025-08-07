package com.pcshop.orderservice.dto;

import java.util.List;

public class UserProfileResponse {

    private String username;
    private String deliveryAddress;  // Match this name to "deliveryAddress" from JSON
    private List<String> roles;

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for deliveryAddress
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    // Getter and Setter for roles
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Optional: toString() override for better logging
    @Override
    public String toString() {
        return "UserProfileResponse{" +
                "username='" + username + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", roles=" + roles +
                '}';
    }
}

package com.mateuszjanczak.barrelsbeer.configuration.security.dto;

import java.util.stream.Collectors;

public class UserResponse {

    private String username;
    private String email;

    public UserResponse() {
    }

    public UserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

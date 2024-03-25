package com.example.enterpriseattendancesystem.response;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String token;
    private String message;

    public LoginResponse(boolean success, String token, String message) {
        this.success = success;
        this.token = token;
        this.message = message;
    }
}

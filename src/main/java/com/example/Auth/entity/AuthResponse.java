package com.example.Auth.entity;

import java.sql.Timestamp;

import lombok.Data;


@Data
//@Builder
public class AuthResponse {
    

    private final String timestamp;
    private final String message;
    private Code code;


    public AuthResponse(String timestamp, Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.label;
        this.code = code;
    }
}

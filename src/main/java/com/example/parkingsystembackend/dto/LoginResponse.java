package com.example.parkingsystembackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private Integer userId;
    private String role;
    private long expiresIn;
    private String name;
}

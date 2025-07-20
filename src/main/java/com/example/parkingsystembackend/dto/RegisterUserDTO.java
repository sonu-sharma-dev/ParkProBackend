package com.example.parkingsystembackend.dto;

import com.example.parkingsystembackend.entity.User.Role;
import lombok.Data;

@Data
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String phoneNumber;
}

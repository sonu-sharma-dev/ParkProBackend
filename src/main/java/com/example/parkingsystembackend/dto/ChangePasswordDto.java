package com.example.parkingsystembackend.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private Integer userId;

    private String currentPassword;

    private String newPassword;

    private String confirmNewPassword;
}
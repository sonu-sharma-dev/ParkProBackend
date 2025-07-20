package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.config.JwtService;
import com.example.parkingsystembackend.dto.ChangePasswordDto;
import com.example.parkingsystembackend.dto.LoginResponse;
import com.example.parkingsystembackend.dto.LoginUserDTO;
import com.example.parkingsystembackend.dto.RegisterUserDTO;
import com.example.parkingsystembackend.entity.User;
import com.example.parkingsystembackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) throws BadRequestException {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse response = LoginResponse.builder()
                    .token(jwtToken)
                    .userId(authenticatedUser.getId())
                    .expiresIn(jwtService.getExpirationTime())
                    .role(authenticatedUser.getRole().name())
                    .name(authenticatedUser.getName())
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect email or password");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while authenticating");
        }
    }


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws UsernameNotFoundException {
        boolean isPasswordChanged = authenticationService.changePassword(changePasswordDto);

        if (isPasswordChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid current password");
        }
    }
}
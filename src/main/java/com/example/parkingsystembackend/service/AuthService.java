package com.example.parkingsystembackend.service;

import com.example.parkingsystembackend.dto.ChangePasswordDto;
import com.example.parkingsystembackend.dto.LoginUserDTO;
import com.example.parkingsystembackend.dto.RegisterUserDTO;
import com.example.parkingsystembackend.entity.User;
import com.example.parkingsystembackend.repository.RoleRepository;
import com.example.parkingsystembackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public User signup(RegisterUserDTO input) throws BadRequestException {
        rejectIfEmailAlreadyExists(input);
        return userRepository.save(mapToUser(input));
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public boolean changePassword(ChangePasswordDto changePasswordDto) throws UsernameNotFoundException {
        User user = userRepository.findById(changePasswordDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(user);

        return true;
    }


    private void rejectIfEmailAlreadyExists(RegisterUserDTO input) throws BadRequestException {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
    }

    private User mapToUser(RegisterUserDTO input) {

        return User.builder()
                .name(input.getName())
                .role(input.getRole())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword().trim()))
                .phoneNumber(input.getPhoneNumber())
                .build();
    }
}
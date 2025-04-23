package com.erikssonherlo.taskmanagement.auth.service.impl;

import com.erikssonherlo.taskmanagement.auth.dto.LoginDTO;
import com.erikssonherlo.taskmanagement.auth.dto.RegisterDTO;
import com.erikssonherlo.taskmanagement.auth.service.AuthService;
import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceAlreadyExistsException;
import com.erikssonherlo.taskmanagement.common.payload.AuthResponse;
import com.erikssonherlo.taskmanagement.common.security.JWTService;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("User", "email", request.email());
        }

        UserEntity newUser = UserEntity.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.MEMBER) // Default role for new users, open to all users.
                .build();

        userRepository.save(newUser);
        String token = jwtService.generateToken(newUser);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            throw new BadRequestException("Invalid email or password.");
        }

        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password."));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}

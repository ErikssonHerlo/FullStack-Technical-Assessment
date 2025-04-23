package com.erikssonherlo.taskmanagement.auth.service;


import com.erikssonherlo.taskmanagement.auth.dto.LoginDTO;
import com.erikssonherlo.taskmanagement.auth.dto.RegisterDTO;
import com.erikssonherlo.taskmanagement.common.payload.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginDTO request);
    AuthResponse register(RegisterDTO request);
}

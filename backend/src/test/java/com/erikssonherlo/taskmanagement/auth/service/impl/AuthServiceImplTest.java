package com.erikssonherlo.taskmanagement.auth.service.impl;

import com.erikssonherlo.taskmanagement.auth.dto.LoginDTO;
import com.erikssonherlo.taskmanagement.auth.dto.RegisterDTO;
import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceAlreadyExistsException;
import com.erikssonherlo.taskmanagement.common.payload.AuthResponse;
import com.erikssonherlo.taskmanagement.common.security.JWTService;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String TOKEN = "jwt-token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------ REGISTER TESTS ------------------------

    @Test
    void register_shouldRegisterUserSuccessfully() {
        RegisterDTO request = new RegisterDTO(EMAIL, "First", "Last", PASSWORD);

        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(TOKEN);

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(TOKEN);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void register_shouldThrowExceptionWhenEmailExists() {
        RegisterDTO request = new RegisterDTO(EMAIL, "First", "Last", PASSWORD);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("User");

        verify(userRepository, never()).save(any());
    }

    // ------------------------ LOGIN TESTS ------------------------

    @Test
    void login_shouldLoginSuccessfully() {
        LoginDTO request = new LoginDTO(EMAIL, PASSWORD);
        UserEntity user = UserEntity.builder()
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .role(Role.MEMBER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(TOKEN);

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(TOKEN);
    }

    @Test
    void login_shouldThrowExceptionWhenAuthenticationFails() {
        LoginDTO request = new LoginDTO(EMAIL, PASSWORD);
        doThrow(new RuntimeException("Auth failed")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    void login_shouldThrowExceptionWhenUserNotFound() {
        LoginDTO request = new LoginDTO(EMAIL, PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid email or password");
    }
}

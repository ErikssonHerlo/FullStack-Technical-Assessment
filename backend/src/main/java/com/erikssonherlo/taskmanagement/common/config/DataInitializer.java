package com.erikssonherlo.taskmanagement.common.config;

import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes the first ADMIN user if it does not exist.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.password:admin123}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        String adminEmail = "admin@taskmanager.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            UserEntity admin = UserEntity.builder()
                    .email(adminEmail)
                    .firstName("Super")
                    .lastName("Admin")
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user created successfully with email: " + adminEmail);
        } else {
            System.out.println("Admin user already exists, skipping creation.");
        }
    }
}

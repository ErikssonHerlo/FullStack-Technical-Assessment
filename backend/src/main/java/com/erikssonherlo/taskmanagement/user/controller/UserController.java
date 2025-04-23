package com.erikssonherlo.taskmanagement.user.controller;

import com.erikssonherlo.taskmanagement.common.payload.ApiResponse;
import com.erikssonherlo.taskmanagement.common.payload.PaginatedResponse;
import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@EnableMethodSecurity
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User Management", description = "Endpoints for managing users (Admin only)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users. Requires ADMIN role.")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users retrieved successfully",
                HttpStatus.OK,
                userService.getAllUsers()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'MEMBER')")
    @Operation(summary = "Get user info", description = "Retrieve information about the authenticated user.")
    public ApiResponse<UserDTO> getUserInfo(Authentication authentication) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                HttpStatus.OK,
                userService.getUserInfo(authentication)
        );
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get paginated users", description = "Retrieve paginated list of users. Requires ADMIN role.")
    public PaginatedResponse<List<UserDTO>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserDTO> userPage = userService.getAllUsersPaginated(page, size);

        return PaginatedResponse.<List<UserDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .status(HttpStatus.OK)
                .data(userPage.getContent())
                .pageable(userPage.getPageable())
                .isFirst(userPage.isFirst())
                .isLast(userPage.isLast())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.hasPrevious())
                .totalPages(userPage.getTotalPages())
                .totalElements((int) userPage.getTotalElements())
                .build();
    }

    @GetMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get users by role (paginated)", description = "Retrieve a paginated list of users by role. Requires ADMIN role.")
    public PaginatedResponse<List<UserDTO>> getUsersByRole(
            @RequestParam Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserDTO> userPage = userService.getUsersByRole(role, page, size);

        return PaginatedResponse.<List<UserDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Users retrieved successfully by role")
                .status(HttpStatus.OK)
                .data(userPage.getContent())
                .pageable(userPage.getPageable())
                .isFirst(userPage.isFirst())
                .isLast(userPage.isLast())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.hasPrevious())
                .totalPages(userPage.getTotalPages())
                .totalElements((int) userPage.getTotalElements())
                .build();
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get user by email", description = "Retrieve a single user by their email. Requires ADMIN role.")
    public ApiResponse<UserDTO> getUserByEmail(@PathVariable String email) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User retrieved successfully",
                HttpStatus.OK,
                userService.getUserByEmail(email)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new user", description = "Create a new user in the system. Requires ADMIN role.")
    public ApiResponse<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User created successfully",
                HttpStatus.OK,
                userService.createUser(userDTO)
        );
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update user", description = "Update an existing user's information by email. Requires ADMIN role.")
    public ApiResponse<UserDTO> updateUser(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "User updated successfully",
                HttpStatus.OK,
                userService.updateUser(email, userDTO)
        );
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete user", description = "Soft delete a user by email (sets deleted_at). Requires ADMIN role.")
    public ApiResponse<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "User deleted successfully (soft delete)",
                HttpStatus.NO_CONTENT,
                null
        );
    }
}

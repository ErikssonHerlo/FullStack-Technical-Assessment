package com.erikssonherlo.taskmanagement.user.dto;

import com.erikssonherlo.taskmanagement.user.model.Role;

public record UserDTO (
    String email,
    String firstName,
    String lastName,
     Role role
) { };

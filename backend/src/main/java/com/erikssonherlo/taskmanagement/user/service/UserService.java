package com.erikssonherlo.taskmanagement.user.service;

import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    Page<UserDTO> getAllUsersPaginated(int page, int size);
    Page<UserDTO> getUsersByRole(Role role, int page, int size);
    UserDTO getUserByEmail(String email);
    UserDTO updateUser(String email, UserDTO userDTO);
    void deleteUser(String email);
}

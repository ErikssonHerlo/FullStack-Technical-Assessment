package com.erikssonherlo.taskmanagement.user.service.impl;

import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceAlreadyExistsException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceNotFoundException;
import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.mapper.UserMapper;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import com.erikssonherlo.taskmanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.email() == null || userDTO.email().isBlank()) {
            throw new BadRequestException("Email is required to create a user.");
        }
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new ResourceAlreadyExistsException("User", "email", userDTO.email());
        }

        System.out.println("DTO: " + userDTO.email());

        UserEntity entity = UserEntity.builder()
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .role(userDTO.role())
                .password(passwordEncoder.encode("default_password"))
                .build();

        UserEntity savedEntity = userRepository.save(entity);

        return new UserDTO(
                savedEntity.getEmail(),
                savedEntity.getFirstName(),
                savedEntity.getLastName(),
                savedEntity.getRole()
        );
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::entityToModel)
                .map(userMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable)
                .map(userEntity -> userMapper.modelToDto(userMapper.entityToModel(userEntity)));
    }


    @Override
    public Page<UserDTO> getUsersByRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAllByRole(role, pageable)
                .map(userEntity -> userMapper.modelToDto(userMapper.entityToModel(userEntity)));
    }


    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.modelToDto(userMapper.entityToModel(entity));
    }

    @Override
    public UserDTO updateUser(String email, UserDTO userDTO) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        entity.setFirstName(userDTO.firstName());
        entity.setLastName(userDTO.lastName());
        entity.setRole(userDTO.role());

        return userMapper.modelToDto(userMapper.entityToModel(userRepository.save(entity)));
    }

    @Override
    public void deleteUser(String email) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (entity.getDeletedAt() != null) {
            throw new ResourceAlreadyExistsException("User", "email", email);
        }

        entity.setDeletedAt(java.time.LocalDateTime.now());
        userRepository.save(entity);
    }

}

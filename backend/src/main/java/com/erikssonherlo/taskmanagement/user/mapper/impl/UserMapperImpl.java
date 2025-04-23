package com.erikssonherlo.taskmanagement.user.mapper.impl;

import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.mapper.UserMapper;
import com.erikssonherlo.taskmanagement.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User entityToModel(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserEntity modelToEntity(User model) {
        if (model == null) return null;

        return UserEntity.builder()
                .email(model.getEmail())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .role(model.getRole())
                .build();
    }

    @Override
    public UserDTO modelToDto(User model) {
        if (model == null) return null;

        return new UserDTO(
                model.getEmail(),
                model.getFirstName(),
                model.getLastName(),
                model.getRole()
        );
    }

    @Override
    public User dtoToModel(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .role(dto.role())
                .build();
    }

    public UserEntity dtoToEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return UserEntity.builder()
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .role(dto.role())
                .build();
    }

    public UserDTO entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserDTO(
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );
    }
}

package com.erikssonherlo.taskmanagement.user.mapper;

import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    User entityToModel(UserEntity entity);
    UserEntity modelToEntity(User model);
    UserDTO modelToDto(User model);
    User dtoToModel(UserDTO dto);

    UserDTO entityToDto(UserEntity entity);
    UserEntity dtoToEntity(UserDTO dto);
}

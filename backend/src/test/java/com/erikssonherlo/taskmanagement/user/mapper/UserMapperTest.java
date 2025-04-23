package com.erikssonherlo.taskmanagement.user.mapper;

import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testDtoToModelMapping() {
        // Arrange
        UserDTO dto = new UserDTO("test@example.com", "John", "Doe", Role.ADMIN);

        // Act
        User model = userMapper.dtoToModel(dto);

        // Assert
        assertNotNull(model);
        assertEquals("test@example.com", model.getEmail());
        assertEquals("John", model.getFirstName());
        assertEquals("Doe", model.getLastName());
        assertEquals(Role.ADMIN, model.getRole());
    }
}

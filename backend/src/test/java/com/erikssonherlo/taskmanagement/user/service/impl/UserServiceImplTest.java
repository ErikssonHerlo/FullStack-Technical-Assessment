package com.erikssonherlo.taskmanagement.user.service.impl;

import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceAlreadyExistsException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceNotFoundException;
import com.erikssonherlo.taskmanagement.user.mapper.UserMapper;
import com.erikssonherlo.taskmanagement.user.dto.UserDTO;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    private final String EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- CREATE TESTS ----------
    @Test
    void createUser_successful() {
        UserDTO userDTO = new UserDTO(EMAIL, "Test", "User", Role.MEMBER);
        UserEntity entity = UserEntity.builder()
                .email(EMAIL)
                .firstName("Test")
                .lastName("User")
                .role(Role.MEMBER)
                .build();

        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        UserDTO result = userService.createUser(userDTO);

        assertThat(result.email()).isEqualTo(EMAIL);
        verify(userRepository).save(userEntityCaptor.capture());
        assertThat(userEntityCaptor.getValue().getPassword()).isEqualTo("encoded_password");
    }

    @Test
    void createUser_shouldThrowBadRequest_whenEmailIsNull() {
        UserDTO userDTO = new UserDTO(null, "Test", "User", Role.MEMBER);
        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email is required");
    }

    @Test
    void createUser_shouldThrowAlreadyExists_whenEmailExists() {
        UserDTO userDTO = new UserDTO(EMAIL, "Test", "User", Role.MEMBER);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);
        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("email");
    }

    // ---------- GET ALL USERS ----------
    @Test
    void getAllUsers_shouldReturnUserList() {
        UserEntity entity = UserEntity.builder().email(EMAIL).build();
        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.entityToModel(entity)).thenReturn(
                new com.erikssonherlo.taskmanagement.user.model.User(
                        entity.getEmail(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getRole()
                )
        );

        when(userMapper.modelToDto(any())).thenAnswer(invocation -> {
            var model = invocation.getArgument(0, com.erikssonherlo.taskmanagement.user.model.User.class);
            return new UserDTO(
                    model.getEmail(),
                    model.getFirstName(),
                    model.getLastName(),
                    model.getRole()
            );
        });


        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).isNotEmpty();
    }

    // ---------- PAGINATED USERS ----------
    @Test
    void getAllUsersPaginated_shouldReturnPage() {
        UserEntity entity = UserEntity.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.MEMBER)
                .build();

        com.erikssonherlo.taskmanagement.user.model.User model = new com.erikssonherlo.taskmanagement.user.model.User(
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );

        UserDTO dto = new UserDTO(
                model.getEmail(),
                model.getFirstName(),
                model.getLastName(),
                model.getRole()
        );

        Page<UserEntity> page = new PageImpl<>(List.of(entity));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(userMapper.entityToModel(entity)).thenReturn(model);
        when(userMapper.modelToDto(model)).thenReturn(dto);

        Page<UserDTO> result = userService.getAllUsersPaginated(0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("test@example.com");
    }


    @Test
    void getUsersByRole_shouldReturnFilteredPage() {
        // Arrange
        UserEntity entity = UserEntity.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.MEMBER)
                .build();

        com.erikssonherlo.taskmanagement.user.model.User model = new com.erikssonherlo.taskmanagement.user.model.User(
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole()
        );

        UserDTO dto = new UserDTO(
                model.getEmail(),
                model.getFirstName(),
                model.getLastName(),
                model.getRole()
        );

        Page<UserEntity> page = new PageImpl<>(List.of(entity));

        when(userRepository.findAllByRole(eq(Role.MEMBER), any(Pageable.class))).thenReturn(page);
        when(userMapper.entityToModel(entity)).thenReturn(model);
        when(userMapper.modelToDto(model)).thenReturn(dto);

        // Act
        Page<UserDTO> result = userService.getUsersByRole(Role.MEMBER, 0, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("test@example.com");
    }

    // ---------- GET USER BY EMAIL ----------
    @Test
    void getUserByEmail_success() {
        UserEntity entity = UserEntity.builder().email(EMAIL).build();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(userMapper.entityToModel(entity)).thenReturn(
                new com.erikssonherlo.taskmanagement.user.model.User(
                        entity.getEmail(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getRole()
                )
        );

        when(userMapper.modelToDto(any())).thenAnswer(invocation -> {
            var model = invocation.getArgument(0, com.erikssonherlo.taskmanagement.user.model.User.class);
            return new UserDTO(
                    model.getEmail(),
                    model.getFirstName(),
                    model.getLastName(),
                    model.getRole()
            );
        });

        UserDTO result = userService.getUserByEmail(EMAIL);

        assertThat(result.email()).isEqualTo(EMAIL);
    }

    @Test
    void getUserByEmail_shouldThrowNotFound_whenMissing() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserByEmail(EMAIL))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("email");
    }

    // ---------- UPDATE USER ----------
    @Test
    void updateUser_success() {
        UserEntity entity = UserEntity.builder().email(EMAIL).firstName("Old").build();
        UserDTO dto = new UserDTO(EMAIL, "New", "User", Role.ADMIN);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
        when(userMapper.entityToModel(entity)).thenReturn(
                new com.erikssonherlo.taskmanagement.user.model.User(
                        entity.getEmail(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getRole()
                )
        );

        when(userMapper.modelToDto(any())).thenAnswer(invocation -> {
            var model = invocation.getArgument(0, com.erikssonherlo.taskmanagement.user.model.User.class);
            return new UserDTO(
                    model.getEmail(),
                    model.getFirstName(),
                    model.getLastName(),
                    model.getRole()
            );
        });

        when(userMapper.dtoToEntity(dto)).thenAnswer(invocation -> {
            var dtoArg = invocation.getArgument(0, UserDTO.class);
            return UserEntity.builder()
                    .email(dtoArg.email())
                    .firstName(dtoArg.firstName())
                    .lastName(dtoArg.lastName())
                    .role(dtoArg.role())
                    .build();
        });

        UserDTO result = userService.updateUser(EMAIL, dto);

        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(entity.getFirstName()).isEqualTo("New");
    }

    @Test
    void updateUser_shouldThrowNotFound_whenMissing() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.updateUser(EMAIL, mock(UserDTO.class)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---------- DELETE USER ----------
    @Test
    void deleteUser_success() {
        UserEntity entity = UserEntity.builder().email(EMAIL).build();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));

        userService.deleteUser(EMAIL);

        assertThat(entity.getDeletedAt()).isNotNull();
        verify(userRepository).save(entity);
    }

    @Test
    void deleteUser_shouldThrowNotFound_whenMissing() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.deleteUser(EMAIL))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteUser_shouldThrowAlreadyExists_whenAlreadyDeleted() {
        UserEntity entity = UserEntity.builder().email(EMAIL).deletedAt(LocalDateTime.now()).build();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        assertThatThrownBy(() -> userService.deleteUser(EMAIL))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }
}

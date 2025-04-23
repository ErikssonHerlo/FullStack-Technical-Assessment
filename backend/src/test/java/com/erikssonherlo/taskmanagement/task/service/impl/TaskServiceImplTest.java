package com.erikssonherlo.taskmanagement.task.service.impl;

import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceNotFoundException;
import com.erikssonherlo.taskmanagement.common.security.JWTService;
import com.erikssonherlo.taskmanagement.task.dto.*;
import com.erikssonherlo.taskmanagement.task.entity.TaskEntity;
import com.erikssonherlo.taskmanagement.task.mapper.TaskMapper;
import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import com.erikssonherlo.taskmanagement.task.repository.TaskRepository;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private JWTService jwtService;

    @Captor
    private ArgumentCaptor<TaskEntity> taskCaptor;

    private static final String JWT = "mocked.jwt.token";
    private static final String USER_EMAIL = "user@example.com";
    private static final String OTHER_EMAIL = "other@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------- CREATE TASK FOR SELF ----------------------

    @Test
    void createTaskForSelf_successful() {
        CreateTaskForSelfDTO dto = new CreateTaskForSelfDTO("Task Title", "Task Description", TaskPriority.MEDIUM, TaskStatus.TO_DO, LocalDateTime.now());
        UserEntity user = createUser(USER_EMAIL, Role.MEMBER);
        TaskEntity taskEntity = TaskEntity.builder().build();
        TaskDTO taskDTO = new TaskDTO(1L, "Task Title", "Task Description", TaskStatus.TO_DO, TaskPriority.MEDIUM, LocalDateTime.now(), USER_EMAIL, USER_EMAIL, LocalDateTime.now(), LocalDateTime.now());

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTaskForSelf(dto, JWT);

        assertThat(result).isNotNull();
        assertThat(result.assignedTo()).isEqualTo(USER_EMAIL);
        verify(taskRepository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getAssignedTo()).isEqualTo(user);
    }

    @Test
    void createTaskForSelf_userNotFound() {
        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTaskForSelf(mock(CreateTaskForSelfDTO.class), JWT))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    // ---------------------- CREATE TASK FOR OTHER ----------------------

    @Test
    void createTaskForOther_successful() {
        CreateTaskForOtherDTO dto = new CreateTaskForOtherDTO("Task Title", "Task Description", TaskPriority.HIGH, TaskStatus.TO_DO, LocalDateTime.now(), OTHER_EMAIL);
        UserEntity manager = createUser(USER_EMAIL, Role.MANAGER);
        UserEntity assignedUser = createUser(OTHER_EMAIL, Role.MEMBER);
        TaskEntity taskEntity = TaskEntity.builder().build();
        TaskDTO taskDTO = new TaskDTO(1L, "Task Title", "Task Description", TaskStatus.TO_DO, TaskPriority.HIGH, LocalDateTime.now(), OTHER_EMAIL, USER_EMAIL, LocalDateTime.now(), LocalDateTime.now());

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MANAGER);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(manager));
        when(userRepository.findByEmail(OTHER_EMAIL)).thenReturn(Optional.of(assignedUser));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
        when(taskMapper.toDTO(taskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTaskForOther(dto, JWT);

        assertThat(result).isNotNull();
        assertThat(result.assignedTo()).isEqualTo(OTHER_EMAIL);
    }

    @Test
    void createTaskForOther_memberCannotAssign() {
        CreateTaskForOtherDTO dto = new CreateTaskForOtherDTO(
                "Task Title", "Task Description", TaskPriority.HIGH, TaskStatus.TO_DO, LocalDateTime.now(), OTHER_EMAIL
        );

        UserEntity memberUser = createUser(USER_EMAIL, Role.MEMBER);

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MEMBER);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(memberUser)); // ✅ Aquí el mock correcto

        assertThatThrownBy(() -> taskService.createTaskForOther(dto, JWT))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Members are not allowed");
    }


    @Test
    void createTaskForOther_roleMissingInToken() {
        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(null);

        assertThatThrownBy(() -> taskService.createTaskForOther(mock(CreateTaskForOtherDTO.class), JWT))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Role not found");
    }

    // ---------------------- GET ALL TASKS ----------------------

    @Test
    void getAllTasks_asMember_onlyOwnTasks() {
        UserEntity member = createUser(USER_EMAIL, Role.MEMBER);
        TaskEntity task1 = TaskEntity.builder().assignedTo(member).build();
        TaskDTO taskDTO1 = new TaskDTO(1L, "Task", "Desc", TaskStatus.TO_DO, TaskPriority.MEDIUM, LocalDateTime.now(), USER_EMAIL, USER_EMAIL, LocalDateTime.now(), LocalDateTime.now());

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MEMBER);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(member));
        when(taskRepository.findAllByAssignedTo(member)).thenReturn(List.of(task1));
        when(taskMapper.toDTO(task1)).thenReturn(taskDTO1);

        List<TaskDTO> result = taskService.getAllTasks(JWT, Optional.empty(), Optional.empty());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).assignedTo()).isEqualTo(USER_EMAIL);
    }

    @Test
    void getAllTasks_admin_excludesAutomanaged() {
        UserEntity admin = createUser(USER_EMAIL, Role.ADMIN);
        UserEntity otherUser = createUser(OTHER_EMAIL, Role.MEMBER);

        TaskEntity automanaged = TaskEntity.builder().createdBy(otherUser).assignedTo(otherUser).build();
        TaskEntity normalTask = TaskEntity.builder().createdBy(admin).assignedTo(otherUser).build();

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.ADMIN);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(admin));
        when(taskRepository.findAll()).thenReturn(List.of(automanaged, normalTask));
        when(taskMapper.toDTO(normalTask)).thenReturn(new TaskDTO(2L, "Normal", "Desc", TaskStatus.TO_DO, TaskPriority.HIGH, LocalDateTime.now(), OTHER_EMAIL, USER_EMAIL, LocalDateTime.now(), LocalDateTime.now()));

        List<TaskDTO> result = taskService.getAllTasks(JWT, Optional.empty(), Optional.empty());

        assertThat(result).hasSize(1); // Only normalTask should be included
    }

    // ---------------------- UPDATE TASK ----------------------

    @Test
    void updateTask_memberCanUpdateStatus() {
        UserEntity member = createUser(USER_EMAIL, Role.MEMBER);
        TaskEntity task = TaskEntity.builder().id(1L).assignedTo(member).createdBy(member).status(TaskStatus.TO_DO).build();
        UpdateTaskDTO dto = new UpdateTaskDTO(null, null, null, TaskStatus.DONE, null);

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MEMBER);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(new TaskDTO(1L, "Task", "Desc", TaskStatus.DONE, TaskPriority.MEDIUM, LocalDateTime.now(), USER_EMAIL, USER_EMAIL, LocalDateTime.now(), LocalDateTime.now()));

        TaskDTO result = taskService.updateTask(1L, dto, JWT);

        assertThat(result.status()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void updateTask_memberCannotUpdateIfNotAssigned() {
        UserEntity member = createUser(USER_EMAIL, Role.MEMBER);
        UserEntity other = createUser(OTHER_EMAIL, Role.MEMBER);
        TaskEntity task = TaskEntity.builder().id(1L).assignedTo(other).createdBy(other).status(TaskStatus.TO_DO).build();

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MEMBER);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTask(1L, new UpdateTaskDTO(null, null, null, TaskStatus.DONE, null), JWT))
                .isInstanceOf(AccessDeniedException.class);
    }

    // ---------------------- DELETE TASK ----------------------

    @Test
    void deleteTask_memberCanDeleteOwnTask() {
        UserEntity member = createUser(USER_EMAIL, Role.MEMBER);
        TaskEntity task = TaskEntity.builder().id(1L).assignedTo(member).createdBy(member).build();

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.MEMBER);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, JWT);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_adminCannotDeleteAutomanagedOfOthers() {
        UserEntity admin = createUser(USER_EMAIL, Role.ADMIN);
        UserEntity other = createUser(OTHER_EMAIL, Role.MEMBER);
        TaskEntity automanaged = TaskEntity.builder().id(1L).assignedTo(other).createdBy(other).build();

        when(jwtService.getUsernameFromToken(JWT)).thenReturn(USER_EMAIL);
        when(jwtService.getRoleFromToken(JWT)).thenReturn(Role.ADMIN);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(automanaged));

        assertThatThrownBy(() -> taskService.deleteTask(1L, JWT))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Cannot delete self-managed task of another user");
    }

    // ---------------------- UTILITY ----------------------

    private UserEntity createUser(String email, Role role) {
        return UserEntity.builder()
                .email(email)
                .role(role)
                .build();
    }
}

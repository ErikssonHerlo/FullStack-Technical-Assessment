package com.erikssonherlo.taskmanagement.task.service.impl;

import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.exception.ResourceNotFoundException;
import com.erikssonherlo.taskmanagement.common.security.JWTService;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForOtherDTO;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForSelfDTO;
import com.erikssonherlo.taskmanagement.task.dto.TaskDTO;
import com.erikssonherlo.taskmanagement.task.dto.UpdateTaskDTO;
import com.erikssonherlo.taskmanagement.task.entity.TaskEntity;
import com.erikssonherlo.taskmanagement.task.mapper.TaskMapper;
import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import com.erikssonherlo.taskmanagement.task.repository.TaskRepository;
import com.erikssonherlo.taskmanagement.task.service.TaskService;
import com.erikssonherlo.taskmanagement.user.model.Role;
import com.erikssonherlo.taskmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Implementation of the TaskService interface.
 * Provides methods for creating, retrieving, updating, and deleting tasks.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final JWTService jwtService;

    @Override
    public TaskDTO createTaskForSelf(CreateTaskForSelfDTO dto, String jwt) {
        String currentUserEmail = jwtService.getUsernameFromToken(jwt);

        var user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        var task = TaskEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .status(dto.status())
                .dueDate(dto.dueDate())
                .createdBy(user)
                .assignedTo(user)
                .build();

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public TaskDTO createTaskForOther(CreateTaskForOtherDTO dto, String jwt) {
        String currentUserEmail = jwtService.getUsernameFromToken(jwt);
        Role currentUserRole = jwtService.getRoleFromToken(jwt);

        if (currentUserRole == null) {
            throw new BadRequestException("Role not found in JWT token.");
        }

        var user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        if (currentUserRole == Role.MEMBER) {
            throw new AccessDeniedException("Members are not allowed to assign tasks to others.");
        }

        var assignedUser = userRepository.findByEmail(dto.assignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", dto.assignedTo()));

        var task = TaskEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .status(dto.status())
                .dueDate(dto.dueDate())
                .createdBy(user)
                .assignedTo(assignedUser)
                .build();

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> getAllTasks(String jwt, Optional<TaskStatus> status, Optional<TaskPriority> priority) {
        String currentUserEmail = jwtService.getUsernameFromToken(jwt);
        Role currentUserRole = jwtService.getRoleFromToken(jwt);

        var userAssignedTo = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        if (currentUserRole == Role.MEMBER) {
            // MEMBER: only can see their own tasks
            return taskRepository
                    .findAllByAssignedTo(userAssignedTo)
                    .stream()
                    .filter(task -> status.map(s -> task.getStatus() == s).orElse(true))
                    .filter(task -> priority.map(p -> task.getPriority() == p).orElse(true))
                    .map(taskMapper::toDTO)
                    .collect(Collectors.toList());
        }

        // ADMIN o MANAGER
        return taskRepository.findAll().stream()
                .filter(task -> {
                    // Filter out tasks that are self-managed by another user
                    // (i.e., created by the same user and assigned to themselves)
                    boolean isAutomanagedByAnother = task.getCreatedBy().getEmail().equals(task.getAssignedTo().getEmail())
                            && !task.getCreatedBy().getEmail().equals(currentUserEmail);
                    return !isAutomanagedByAnother;
                })
                .filter(task -> status.map(s -> task.getStatus() == s).orElse(true))
                .filter(task -> priority.map(p -> task.getPriority() == p).orElse(true))
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long taskId, UpdateTaskDTO dto, String jwt) {
        String currentUserEmail = jwtService.getUsernameFromToken(jwt);
        Role currentUserRole = jwtService.getRoleFromToken(jwt);

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        boolean isOwnTask = task.getAssignedTo().getEmail().equals(currentUserEmail);
        boolean isAutomanaged = task.getAssignedTo().getEmail().equals(task.getCreatedBy().getEmail());

        if (currentUserRole == Role.MEMBER) {
            // MEMBER solo puede actualizar status de sus propias tareas asignadas
            if (!isOwnTask) {
                throw new AccessDeniedException("You can only update your own assigned tasks.");
            }
            if (dto.status() == null) {
                throw new BadRequestException("Status is required for status update.");
            }
            task.setStatus(dto.status());
        } else {
            // ADMIN / MANAGER no pueden modificar tareas autogestionadas ajenas
            if (isAutomanaged && !task.getCreatedBy().getEmail().equals(currentUserEmail)) {
                throw new AccessDeniedException("Cannot modify self-managed task of another user.");
            }

            // Update only provided fields (PATCH)
            if (dto.title() != null) task.setTitle(dto.title());
            if (dto.description() != null) task.setDescription(dto.description());
            if (dto.priority() != null) task.setPriority(dto.priority());
            if (dto.status() != null) task.setStatus(dto.status());
            if (dto.dueDate() != null) task.setDueDate(dto.dueDate());
        }

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long taskId, String jwt) {
        String currentUserEmail = jwtService.getUsernameFromToken(jwt);
        Role currentUserRole = jwtService.getRoleFromToken(jwt);

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        boolean isOwnTask = task.getAssignedTo().getEmail().equals(currentUserEmail);
        boolean isAutomanaged = task.getAssignedTo().getEmail().equals(task.getCreatedBy().getEmail());

        if (currentUserRole == Role.MEMBER) {
            // MEMBER: only can delete their own self-managed tasks
            if (!isOwnTask || !isAutomanaged) {
                throw new AccessDeniedException("You can only delete your own self-managed tasks.");
            }
        } else {
            // ADMIN / MANAGER cannot delete self-managed tasks of others
            if (isAutomanaged && !task.getCreatedBy().getEmail().equals(currentUserEmail)) {
                throw new AccessDeniedException("Cannot delete self-managed task of another user.");
            }
        }

        taskRepository.delete(task);
    }





}

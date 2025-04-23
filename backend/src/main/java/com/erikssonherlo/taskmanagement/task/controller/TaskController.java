package com.erikssonherlo.taskmanagement.task.controller;

import com.erikssonherlo.taskmanagement.common.exception.BadRequestException;
import com.erikssonherlo.taskmanagement.common.payload.ApiResponse;
import com.erikssonherlo.taskmanagement.common.security.JWTService;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForOtherDTO;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForSelfDTO;
import com.erikssonherlo.taskmanagement.task.dto.TaskDTO;
import com.erikssonherlo.taskmanagement.task.dto.UpdateTaskDTO;
import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import com.erikssonherlo.taskmanagement.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Endpoints for managing tasks with role-based permissions")
@EnableMethodSecurity
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'MEMBER')")
    @PostMapping("/self")
    @Operation(summary = "Create task for self", description = "Create a task for the current authenticated user")
    public ApiResponse<TaskDTO> createTaskForSelf(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody @Valid CreateTaskForSelfDTO dto
    ) {
        String token = extractToken(authHeader);
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Task created successfully",
                HttpStatus.OK,
                taskService.createTaskForSelf(dto, token)
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("")
    @Operation(summary = "Create task for another user", description = "Create a task assigned to another user (Manager/Admin only)")
    public ApiResponse<TaskDTO> createTaskForOther(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody @Valid CreateTaskForOtherDTO dto
    ) {
        String token = extractToken(authHeader);
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Task created successfully for other user",
                HttpStatus.OK,
                taskService.createTaskForOther(dto, token)
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'MEMBER')")
    @GetMapping
    @Operation(summary = "Get tasks", description = "Retrieve tasks with optional filters by status and priority")
    public ApiResponse<List<TaskDTO>> getAllTasks(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority
    ) {
        String token = extractToken(authHeader);
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Tasks retrieved successfully",
                HttpStatus.OK,
                taskService.getAllTasks(token, Optional.ofNullable(status), Optional.ofNullable(priority))
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'MEMBER')")
    @PatchMapping("/{taskId}")
    @Operation(summary = "Update task", description = "Partially update a task by ID (fields optional, role-based restrictions apply)")
    public ApiResponse<TaskDTO> updateTask(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable Long taskId,
            @RequestBody @Valid UpdateTaskDTO dto
    ) {
        String token = extractToken(authHeader);
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Task updated successfully",
                HttpStatus.OK,
                taskService.updateTask(taskId, dto, token)
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'MEMBER')")
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Delete a task by ID (role-based restrictions apply)")
    public ApiResponse<Void> deleteTask(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable Long taskId
    ) {
        String token = extractToken(authHeader);
        taskService.deleteTask(taskId, token);
        return new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Task deleted successfully",
                HttpStatus.NO_CONTENT,
                null
        );
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new BadRequestException("Missing or invalid Authorization header.");
    }
}

package com.erikssonherlo.taskmanagement.task.dto;

import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateTaskDTO(
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        LocalDateTime dueDate
) {
}
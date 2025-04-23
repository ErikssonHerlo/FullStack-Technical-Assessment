package com.erikssonherlo.taskmanagement.task.dto;

import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CreateTaskForSelfDTO(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        @NotNull(message = "Priority is required")
        TaskPriority priority,
        @NotNull(message = "Status is required")
        TaskStatus status,
        LocalDateTime dueDate
) {
}

package com.erikssonherlo.taskmanagement.task.dto;

import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Data Transfer Object for Task.
 * This class is used to transfer task data between different layers of the application.
 * It contains all the necessary fields to represent a task.
 *
 * @param id          The unique identifier of the task.
 * @param title       The title of the task.
 * @param description A brief description of the task.
 * @param status      The current status of the task (e.g., TO_DO, IN_PROGRESS, DONE).
 * @param priority    The priority level of the task (e.g., LOW, MEDIUM, HIGH).
 * @param dueDate     The due date for the task completion.
 * @param assignedTo  The email address of the user to whom the task is assigned.
 * @param createdBy   The email address of the user who created the task.
 * @param createdAt   The timestamp when the task was created.
 * @param updatedAt   The timestamp when the task was last updated.
 */
@Builder
public record TaskDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        String assignedTo,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

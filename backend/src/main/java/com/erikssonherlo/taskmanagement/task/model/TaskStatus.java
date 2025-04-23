package com.erikssonherlo.taskmanagement.task.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum TaskStatus {
    TO_DO,
    IN_PROGRESS,
    REVIEW,
    DONE,
    CANCELLED
}

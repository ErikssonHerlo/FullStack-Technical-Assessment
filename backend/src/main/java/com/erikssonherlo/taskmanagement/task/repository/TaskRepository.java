package com.erikssonherlo.taskmanagement.task.repository;

import com.erikssonherlo.taskmanagement.task.entity.TaskEntity;
import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing TaskEntity persistence.
 * Provides methods to query tasks by assigned user, status, and priority.
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Find all tasks assigned to a specific user.
     *
     * @param assignedTo The user entity to whom the tasks are assigned.
     * @return List of TaskEntity objects.
     */
    List<TaskEntity> findAllByAssignedTo(UserEntity assignedTo);

    /**
     * Find all tasks with a specific status.
     *
     * @param status The status of the tasks.
     * @return List of TaskEntity objects.
     */
    List<TaskEntity> findAllByStatus(TaskStatus status);

    /**
     * Find all tasks assigned to a specific user with a given priority.
     *
     * @param assignedTo The user entity to whom the tasks are assigned.
     * @param priority The priority level of the tasks.
     * @return List of TaskEntity objects.
     */
    List<TaskEntity> findAllByAssignedToAndPriority(UserEntity assignedTo, TaskPriority priority);
}

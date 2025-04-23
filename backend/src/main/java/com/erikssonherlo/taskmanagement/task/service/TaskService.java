package com.erikssonherlo.taskmanagement.task.service;

import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForOtherDTO;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForSelfDTO;
import com.erikssonherlo.taskmanagement.task.dto.TaskDTO;
import com.erikssonherlo.taskmanagement.task.dto.UpdateTaskDTO;
import com.erikssonherlo.taskmanagement.task.model.TaskPriority;
import com.erikssonherlo.taskmanagement.task.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskDTO createTaskForSelf(CreateTaskForSelfDTO dto, String jwt);
    TaskDTO createTaskForOther(CreateTaskForOtherDTO dto, String jwt);
    List<TaskDTO> getAllTasks(String jwt, Optional<TaskStatus> status, Optional<TaskPriority> priority);
    TaskDTO updateTask(Long taskId, UpdateTaskDTO dto, String jwt);
    void deleteTask(Long taskId, String jwt);
}

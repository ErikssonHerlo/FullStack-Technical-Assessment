package com.erikssonherlo.taskmanagement.task.mapper;

import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForOtherDTO;
import com.erikssonherlo.taskmanagement.task.dto.CreateTaskForSelfDTO;
import com.erikssonherlo.taskmanagement.task.dto.TaskDTO;
import com.erikssonherlo.taskmanagement.task.entity.TaskEntity;
import com.erikssonherlo.taskmanagement.task.mapper.TaskMapper;
import com.erikssonherlo.taskmanagement.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper{

    public TaskEntity fromSelfDto(CreateTaskForSelfDTO dto, UserEntity owner) {
        return TaskEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .status(dto.status())
                .dueDate(dto.dueDate())
                .createdBy(owner)
                .assignedTo(owner)
                .build();
    }

    public TaskEntity fromOtherDto(CreateTaskForOtherDTO dto, UserEntity creator, UserEntity assignedTo) {
        return TaskEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .status(dto.status())
                .dueDate(dto.dueDate())
                .createdBy(creator)
                .assignedTo(assignedTo)
                .build();
    }

    public TaskDTO toDTO(TaskEntity entity) {
        return TaskDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .dueDate(entity.getDueDate())
                .createdBy(entity.getCreatedBy().getEmail())
                .assignedTo(entity.getAssignedTo().getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}

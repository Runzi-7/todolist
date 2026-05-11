package com.runzi.todolist.dto;

import java.time.LocalDateTime;

import com.runzi.todolist.entity.TaskPriority;
import com.runzi.todolist.entity.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String categoryName,
        String categoryColor,
        LocalDateTime dueTime,
        LocalDateTime reminderTime,
        Boolean reminded,
        LocalDateTime completedTime,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) {
}

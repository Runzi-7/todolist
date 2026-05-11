package com.runzi.todolist.service;

import java.util.List;

import com.runzi.todolist.dto.TaskResponse;
import com.runzi.todolist.entity.Category;
import com.runzi.todolist.entity.Task;
import com.runzi.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAllByOrderByDueTimeAscCreatedTimeDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(Task task) {
        Category category = task.getCategory();
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                category == null ? null : category.getName(),
                category == null ? null : category.getColor(),
                task.getDueTime(),
                task.getReminderTime(),
                task.getReminded(),
                task.getCompletedTime(),
                task.getCreatedTime(),
                task.getUpdatedTime()
        );
    }
}

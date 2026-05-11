package com.runzi.todolist.controller;

import java.util.List;

import com.runzi.todolist.dto.TaskResponse;
import com.runzi.todolist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponse> findAll() {
        return taskService.findAll();
    }
}

package com.runzi.todolist.repository;

import java.util.List;

import com.runzi.todolist.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = "category")
    List<Task> findAllByOrderByDueTimeAscCreatedTimeDesc();
}

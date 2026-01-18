package com.example.users.controller;

import com.example.users.dto.TaskRequest;
import com.example.users.dto.TaskResponse;
import com.example.users.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API для управления задачами")
public class TaskController {
    
    private final TaskService taskService;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @PostMapping
    @Operation(summary = "Создать задачу")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @RequestParam UUID createdById) {
        TaskResponse response = taskService.createTask(request, createdById);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Получить все задачи")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/published")
    @Operation(summary = "Получить опубликованные задачи")
    public ResponseEntity<List<TaskResponse>> getPublishedTasks() {
        List<TaskResponse> tasks = taskService.getPublishedTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/lesson/{lessonId}")
    @Operation(summary = "Получить задачи по уроку")
    public ResponseEntity<List<TaskResponse>> getTasksByLesson(@PathVariable UUID lessonId) {
        List<TaskResponse> tasks = taskService.getTasksByLesson(lessonId);
        return ResponseEntity.ok(tasks);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request,
            @RequestParam UUID currentUserId) {
        TaskResponse response = taskService.updateTask(id, request, currentUserId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, @RequestParam UUID currentUserId) {
        taskService.deleteTask(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}


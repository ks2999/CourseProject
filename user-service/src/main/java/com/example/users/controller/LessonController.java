package com.example.users.controller;

import com.example.users.dto.LessonRequest;
import com.example.users.dto.LessonResponse;
import com.example.users.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@Tag(name = "Lessons", description = "API для управления уроками")
public class LessonController {
    
    private final LessonService lessonService;
    
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }
    
    @PostMapping
    @Operation(summary = "Создать урок", description = "Создает новый урок (только для преподавателей)")
    public ResponseEntity<LessonResponse> createLesson(
            @Valid @RequestBody LessonRequest request,
            @Parameter(description = "ID создателя урока") @RequestParam UUID createdById) {
        LessonResponse response = lessonService.createLesson(request, createdById);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить урок по ID")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable UUID id) {
        LessonResponse response = lessonService.getLessonById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Получить все уроки")
    public ResponseEntity<List<LessonResponse>> getAllLessons() {
        List<LessonResponse> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/published")
    @Operation(summary = "Получить опубликованные уроки")
    public ResponseEntity<List<LessonResponse>> getPublishedLessons() {
        List<LessonResponse> lessons = lessonService.getPublishedLessons();
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/topic/{topic}")
    @Operation(summary = "Получить уроки по теме")
    public ResponseEntity<List<LessonResponse>> getLessonsByTopic(@PathVariable String topic) {
        List<LessonResponse> lessons = lessonService.getLessonsByTopic(topic);
        return ResponseEntity.ok(lessons);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить урок")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable UUID id,
            @Valid @RequestBody LessonRequest request,
            @RequestParam UUID currentUserId) {
        LessonResponse response = lessonService.updateLesson(id, request, currentUserId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить урок")
    public ResponseEntity<Void> deleteLesson(@PathVariable UUID id, @RequestParam UUID currentUserId) {
        lessonService.deleteLesson(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}


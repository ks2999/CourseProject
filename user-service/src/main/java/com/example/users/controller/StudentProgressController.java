package com.example.users.controller;

import com.example.users.dto.StudentProgressResponse;
import com.example.users.service.StudentProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
@Tag(name = "Student Progress", description = "API для работы с прогрессом студентов")
public class StudentProgressController {
    
    private final StudentProgressService studentProgressService;
    
    public StudentProgressController(StudentProgressService studentProgressService) {
        this.studentProgressService = studentProgressService;
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить прогресс пользователя")
    public ResponseEntity<StudentProgressResponse> getProgress(@PathVariable UUID userId) {
        StudentProgressResponse response = studentProgressService.getProgress(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/leaderboard")
    @Operation(summary = "Получить лидерборд по опыту")
    public ResponseEntity<List<StudentProgressResponse>> getLeaderboard() {
        List<StudentProgressResponse> leaderboard = studentProgressService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    
    @GetMapping("/leaderboard/level")
    @Operation(summary = "Получить лидерборд по уровню")
    public ResponseEntity<List<StudentProgressResponse>> getLeaderboardByLevel() {
        List<StudentProgressResponse> leaderboard = studentProgressService.getLeaderboardByLevel();
        return ResponseEntity.ok(leaderboard);
    }
}


package com.example.users.controller;

import com.example.users.dto.SubmissionRequest;
import com.example.users.dto.SubmissionResponse;
import com.example.users.service.SubmissionService;
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
@RequestMapping("/api/submissions")
@Tag(name = "Submissions", description = "API для отправки решений задач")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }
    
    @PostMapping
    @Operation(summary = "Отправить решение задачи")
    public ResponseEntity<SubmissionResponse> submitSolution(
            @Valid @RequestBody SubmissionRequest request,
            @Parameter(description = "ID пользователя", required = true)
            @RequestParam UUID userId) {
        SubmissionResponse response = submissionService.submitSolution(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить решение по ID")
    public ResponseEntity<SubmissionResponse> getSubmissionById(@PathVariable UUID id) {
        SubmissionResponse response = submissionService.getSubmissionById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить все решения пользователя")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByUser(@PathVariable UUID userId) {
        List<SubmissionResponse> submissions = submissionService.getSubmissionsByUser(userId);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/task/{taskId}")
    @Operation(summary = "Получить все решения задачи")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByTask(@PathVariable UUID taskId) {
        List<SubmissionResponse> submissions = submissionService.getSubmissionsByTask(taskId);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/user/{userId}/task/{taskId}/latest")
    @Operation(summary = "Получить последнее решение пользователя для задачи")
    public ResponseEntity<SubmissionResponse> getLatestSubmission(
            @PathVariable UUID userId,
            @PathVariable UUID taskId) {
        SubmissionResponse response = submissionService.getLatestSubmission(userId, taskId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}


package com.example.users.controller;

import com.example.users.dto.ChallengeRequest;
import com.example.users.dto.ChallengeResponse;
import com.example.users.service.ChallengeService;
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
@RequestMapping("/api/challenges")
@Tag(name = "Challenges", description = "API для управления соревнованиями")
public class ChallengeController {
    
    private final ChallengeService challengeService;
    
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }
    
    @PostMapping
    @Operation(summary = "Создать соревнование", description = "Только для преподавателей и админов")
    public ResponseEntity<ChallengeResponse> createChallenge(
            @Valid @RequestBody ChallengeRequest request,
            @Parameter(description = "ID создателя", required = true)
            @RequestParam UUID createdById) {
        ChallengeResponse response = challengeService.createChallenge(request, createdById);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить соревнование по ID")
    public ResponseEntity<ChallengeResponse> getChallengeById(@PathVariable UUID id) {
        ChallengeResponse response = challengeService.getChallengeById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Получить все соревнования")
    public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
        List<ChallengeResponse> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Получить активные соревнования")
    public ResponseEntity<List<ChallengeResponse>> getActiveChallenges() {
        List<ChallengeResponse> challenges = challengeService.getActiveChallenges();
        return ResponseEntity.ok(challenges);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить соревнование")
    public ResponseEntity<ChallengeResponse> updateChallenge(
            @PathVariable UUID id,
            @Valid @RequestBody ChallengeRequest request,
            @Parameter(description = "ID текущего пользователя", required = true)
            @RequestParam UUID currentUserId) {
        ChallengeResponse response = challengeService.updateChallenge(id, request, currentUserId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить соревнование")
    public ResponseEntity<Void> deleteChallenge(
            @PathVariable UUID id,
            @Parameter(description = "ID текущего пользователя", required = true)
            @RequestParam UUID currentUserId) {
        challengeService.deleteChallenge(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}


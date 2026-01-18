package com.example.users.controller;

import com.example.users.dto.SkillRequest;
import com.example.users.dto.SkillResponse;
import com.example.users.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Skills", description = "API для управления навыками")
public class SkillController {
    
    private final SkillService skillService;
    
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    
    @PostMapping
    @Operation(summary = "Создать навык")
    public ResponseEntity<SkillResponse> createSkill(@Valid @RequestBody SkillRequest request) {
        SkillResponse response = skillService.createSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить навык по ID")
    public ResponseEntity<SkillResponse> getSkillById(@PathVariable UUID id) {
        SkillResponse response = skillService.getSkillById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Получить навык по названию")
    public ResponseEntity<SkillResponse> getSkillByName(@PathVariable String name) {
        SkillResponse response = skillService.getSkillByName(name);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Получить все навыки")
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        List<SkillResponse> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skills);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить навык")
    public ResponseEntity<SkillResponse> updateSkill(
            @PathVariable UUID id,
            @Valid @RequestBody SkillRequest request) {
        SkillResponse response = skillService.updateSkill(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить навык")
    public ResponseEntity<Void> deleteSkill(@PathVariable UUID id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}


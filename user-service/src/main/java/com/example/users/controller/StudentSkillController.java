package com.example.users.controller;

import com.example.users.dto.StudentSkillResponse;
import com.example.users.service.StudentSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student-skills")
@Tag(name = "Student Skills", description = "API для работы с навыками студентов")
public class StudentSkillController {
    
    private final StudentSkillService studentSkillService;
    
    public StudentSkillController(StudentSkillService studentSkillService) {
        this.studentSkillService = studentSkillService;
    }
    
    @PostMapping("/user/{userId}/skill/{skillId}/experience")
    @Operation(summary = "Добавить опыт к навыку студента")
    public ResponseEntity<StudentSkillResponse> addExperience(
            @PathVariable UUID userId,
            @PathVariable UUID skillId,
            @RequestParam int experience) {
        StudentSkillResponse response = studentSkillService.addExperienceToSkill(userId, skillId, experience);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить все навыки студента")
    public ResponseEntity<List<StudentSkillResponse>> getStudentSkills(@PathVariable UUID userId) {
        List<StudentSkillResponse> skills = studentSkillService.getStudentSkills(userId);
        return ResponseEntity.ok(skills);
    }
    
    @GetMapping("/user/{userId}/skill/{skillId}")
    @Operation(summary = "Получить навык студента")
    public ResponseEntity<StudentSkillResponse> getStudentSkill(
            @PathVariable UUID userId,
            @PathVariable UUID skillId) {
        StudentSkillResponse response = studentSkillService.getStudentSkill(userId, skillId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}


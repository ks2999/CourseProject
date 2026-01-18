package com.example.users.dto;

import com.example.users.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Запрос на создание/обновление задачи")
public class TaskRequest {
    
    @Schema(description = "Название задачи", example = "Сумма чисел")
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @Schema(description = "Описание задачи", example = "Напишите функцию для вычисления суммы двух чисел")
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    @Schema(description = "Шаблон кода для начала")
    private String codeTemplate;
    
    @Schema(description = "Тесты в формате JSON")
    private String testCases;
    
    @Schema(description = "Опыт за решение", example = "10")
    private Integer xpReward = 10;
    
    @Schema(description = "Сложность задачи", example = "EASY")
    @NotNull(message = "Difficulty is required")
    private Task.Difficulty difficulty = Task.Difficulty.EASY;
    
    @Schema(description = "ID урока, к которому относится задача")
    private UUID lessonId;
    
    @Schema(description = "ID навыка, который развивает задача")
    private UUID skillId;
    
    @Schema(description = "Опубликована ли задача", example = "false")
    private Boolean published = false;
    
    public TaskRequest() {
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCodeTemplate() {
        return codeTemplate;
    }
    
    public void setCodeTemplate(String codeTemplate) {
        this.codeTemplate = codeTemplate;
    }
    
    public String getTestCases() {
        return testCases;
    }
    
    public void setTestCases(String testCases) {
        this.testCases = testCases;
    }
    
    public Integer getXpReward() {
        return xpReward;
    }
    
    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }
    
    public Task.Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Task.Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public UUID getLessonId() {
        return lessonId;
    }
    
    public void setLessonId(UUID lessonId) {
        this.lessonId = lessonId;
    }
    
    public UUID getSkillId() {
        return skillId;
    }
    
    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }
    
    public Boolean getPublished() {
        return published;
    }
    
    public void setPublished(Boolean published) {
        this.published = published;
    }
}


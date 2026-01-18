package com.example.users.dto;

import com.example.users.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ с данными задачи")
public class TaskResponse {
    
    private UUID id;
    private String title;
    private String description;
    private String codeTemplate;
    private Integer xpReward;
    private Task.Difficulty difficulty;
    private UUID lessonId;
    private String lessonTitle;
    private UUID skillId;
    private String skillName;
    private UUID createdById;
    private String createdByName;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public TaskResponse() {
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
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
    
    public String getLessonTitle() {
        return lessonTitle;
    }
    
    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }
    
    public UUID getSkillId() {
        return skillId;
    }
    
    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public UUID getCreatedById() {
        return createdById;
    }
    
    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    
    public Boolean getPublished() {
        return published;
    }
    
    public void setPublished(Boolean published) {
        this.published = published;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


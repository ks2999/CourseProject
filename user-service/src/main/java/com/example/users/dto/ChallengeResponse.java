package com.example.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ с информацией о соревновании")
public class ChallengeResponse {
    
    @Schema(description = "ID соревнования")
    private UUID id;
    
    @Schema(description = "Название соревнования")
    private String title;
    
    @Schema(description = "Описание соревнования")
    private String description;
    
    @Schema(description = "Тип соревнования")
    private String type;
    
    @Schema(description = "Описание типа")
    private String typeDescription;
    
    @Schema(description = "Список задач соревнования")
    private List<TaskInfo> tasks;
    
    @Schema(description = "Дата начала")
    private LocalDateTime startDate;
    
    @Schema(description = "Дата окончания")
    private LocalDateTime endDate;
    
    @Schema(description = "Дополнительный опыт за участие")
    private Integer xpReward;
    
    @Schema(description = "Активно ли соревнование")
    private Boolean active;
    
    @Schema(description = "Активно ли сейчас (по датам)")
    private Boolean isCurrentlyActive;
    
    @Schema(description = "ID создателя")
    private UUID createdById;
    
    @Schema(description = "Имя создателя")
    private String createdByName;
    
    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;
    
    @Schema(description = "Дата обновления")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Краткая информация о задаче")
    public static class TaskInfo {
        private UUID id;
        private String title;
        private String difficulty;
        private Integer xpReward;
        
        public TaskInfo(UUID id, String title, String difficulty, Integer xpReward) {
            this.id = id;
            this.title = title;
            this.difficulty = difficulty;
            this.xpReward = xpReward;
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
        
        public String getDifficulty() {
            return difficulty;
        }
        
        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
        
        public Integer getXpReward() {
            return xpReward;
        }
        
        public void setXpReward(Integer xpReward) {
            this.xpReward = xpReward;
        }
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTypeDescription() {
        return typeDescription;
    }
    
    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }
    
    public List<TaskInfo> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<TaskInfo> tasks) {
        this.tasks = tasks;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public Integer getXpReward() {
        return xpReward;
    }
    
    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Boolean getIsCurrentlyActive() {
        return isCurrentlyActive;
    }
    
    public void setIsCurrentlyActive(Boolean isCurrentlyActive) {
        this.isCurrentlyActive = isCurrentlyActive;
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


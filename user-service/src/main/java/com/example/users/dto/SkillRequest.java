package com.example.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание/обновление навыка")
public class SkillRequest {
    
    @Schema(description = "Название навыка", example = "Циклы")
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;
    
    @Schema(description = "Описание навыка", example = "Умение работать с циклами for, while, do-while")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    @Schema(description = "Максимальный уровень навыка", example = "10")
    private Integer maxLevel = 10;
    
    public SkillRequest() {
    }
    
    public SkillRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getMaxLevel() {
        return maxLevel;
    }
    
    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }
}


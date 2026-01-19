package com.example.users.dto;

import com.example.users.model.Challenge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Запрос на создание/обновление соревнования")
public class ChallengeRequest {
    
    @NotBlank(message = "Название обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    @Schema(description = "Название соревнования", example = "Соревнование по циклам")
    private String title;
    
    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(description = "Описание соревнования")
    private String description;
    
    @NotNull(message = "Тип соревнования обязателен")
    @Schema(description = "Тип соревнования", example = "THEMATIC")
    private Challenge.Type type;
    
    @NotNull(message = "Список задач обязателен")
    @Size(min = 1, message = "Должна быть хотя бы одна задача")
    @Schema(description = "Список ID задач для соревнования")
    private List<UUID> taskIds;
    
    @NotNull(message = "Дата начала обязательна")
    @Schema(description = "Дата и время начала соревнования")
    private LocalDateTime startDate;
    
    @NotNull(message = "Дата окончания обязательна")
    @Schema(description = "Дата и время окончания соревнования")
    private LocalDateTime endDate;
    
    @Schema(description = "Дополнительный опыт за участие", example = "100")
    private Integer xpReward = 50;
    
    @Schema(description = "Активно ли соревнование", example = "true")
    private Boolean active = true;
    
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
    
    public Challenge.Type getType() {
        return type;
    }
    
    public void setType(Challenge.Type type) {
        this.type = type;
    }
    
    public List<UUID> getTaskIds() {
        return taskIds;
    }
    
    public void setTaskIds(List<UUID> taskIds) {
        this.taskIds = taskIds;
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
}


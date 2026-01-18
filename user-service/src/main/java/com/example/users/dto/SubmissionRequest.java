package com.example.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Запрос на отправку решения задачи")
public class SubmissionRequest {
    
    @Schema(description = "ID задачи")
    @NotNull(message = "Task ID is required")
    private UUID taskId;
    
    @Schema(description = "Код решения")
    @NotBlank(message = "Code is required")
    private String code;
    
    public SubmissionRequest() {
    }
    
    public SubmissionRequest(UUID taskId, String code) {
        this.taskId = taskId;
        this.code = code;
    }
    
    public UUID getTaskId() {
        return taskId;
    }
    
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}


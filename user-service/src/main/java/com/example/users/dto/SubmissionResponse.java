package com.example.users.dto;

import com.example.users.model.Submission;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ с данными решения задачи")
public class SubmissionResponse {
    
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID taskId;
    private String taskTitle;
    private String code;
    private Submission.Status status;
    private String testResults;
    private String errorMessage;
    private Integer testsPassed;
    private Integer testsTotal;
    private Boolean xpAwarded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public SubmissionResponse() {
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public UUID getTaskId() {
        return taskId;
    }
    
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskTitle() {
        return taskTitle;
    }
    
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Submission.Status getStatus() {
        return status;
    }
    
    public void setStatus(Submission.Status status) {
        this.status = status;
    }
    
    public String getTestResults() {
        return testResults;
    }
    
    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Integer getTestsPassed() {
        return testsPassed;
    }
    
    public void setTestsPassed(Integer testsPassed) {
        this.testsPassed = testsPassed;
    }
    
    public Integer getTestsTotal() {
        return testsTotal;
    }
    
    public void setTestsTotal(Integer testsTotal) {
        this.testsTotal = testsTotal;
    }
    
    public Boolean getXpAwarded() {
        return xpAwarded;
    }
    
    public void setXpAwarded(Boolean xpAwarded) {
        this.xpAwarded = xpAwarded;
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


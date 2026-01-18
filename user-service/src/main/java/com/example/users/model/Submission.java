package com.example.users.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Решение задачи студентом
 */
@Entity
@Table(name = "submissions")
public class Submission {
    
    public enum Status {
        PENDING("На проверке"),
        PASSED("Пройдено"),
        FAILED("Не пройдено"),
        ERROR("Ошибка компиляции");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code; // Код решения
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String testResults; // JSON с результатами тестов
    
    @Column
    private String errorMessage; // Сообщение об ошибке, если есть
    
    @Column
    private Integer testsPassed = 0; // Количество пройденных тестов
    
    @Column
    private Integer testsTotal = 0; // Всего тестов
    
    @Column
    private Boolean xpAwarded = false; // Начислен ли опыт
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public Submission() {
    }
    
    public Submission(User user, Task task, String code) {
        this.user = user;
        this.task = task;
        this.code = code;
    }
    
    /**
     * Проверяет, прошло ли решение все тесты
     */
    public boolean isPassed() {
        return status == Status.PASSED;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Task getTask() {
        return task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
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


package com.example.users.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Прогресс студента (общий опыт, уровень, стрик)
 */
@Entity
@Table(name = "student_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id"})
})
public class StudentProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false)
    private Integer totalXp = 0; // Общий опыт
    
    @Column(nullable = false)
    private Integer level = 1; // Общий уровень
    
    @Column(nullable = false)
    private Integer currentStreak = 0; // Текущая серия дней
    
    @Column(nullable = false)
    private Integer maxStreak = 0; // Максимальная серия дней
    
    @Column
    private LocalDateTime lastActivityDate; // Последняя активность
    
    @Column
    private Integer tasksCompleted = 0; // Количество решённых задач
    
    @Column
    private Integer lessonsCompleted = 0; // Количество пройденных уроков
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public StudentProgress() {
    }
    
    public StudentProgress(User user) {
        this.user = user;
    }
    
    /**
     * Добавить опыт и проверить повышение уровня
     * @param xp количество опыта
     */
    public void addXp(int xp) {
        this.totalXp += xp;
        checkLevelUp();
    }
    
    /**
     * Проверка повышения уровня
     * Формула: уровень = sqrt(totalXp / 100)
     */
    private void checkLevelUp() {
        int newLevel = (int) Math.sqrt(totalXp / 100.0) + 1;
        if (newLevel > level) {
            this.level = newLevel;
        }
    }
    
    /**
     * Обновить стрик (серию дней)
     */
    public void updateStreak() {
        LocalDateTime now = LocalDateTime.now();
        
        if (lastActivityDate == null) {
            // Первая активность
            currentStreak = 1;
            maxStreak = 1;
        } else {
            LocalDateTime yesterday = now.minusDays(1);
            LocalDateTime lastDate = lastActivityDate.toLocalDate().atStartOfDay();
            LocalDateTime today = now.toLocalDate().atStartOfDay();
            
            if (lastDate.equals(today)) {
                // Уже активен сегодня, ничего не меняем
                return;
            } else if (lastDate.equals(yesterday.toLocalDate().atStartOfDay())) {
                // Продолжаем стрик
                currentStreak++;
            } else {
                // Стрик прерван
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak;
                }
                currentStreak = 1;
            }
        }
        
        lastActivityDate = now;
        if (currentStreak > maxStreak) {
            maxStreak = currentStreak;
        }
    }
    
    /**
     * Увеличить счётчик решённых задач
     */
    public void incrementTasksCompleted() {
        this.tasksCompleted++;
    }
    
    /**
     * Увеличить счётчик пройденных уроков
     */
    public void incrementLessonsCompleted() {
        this.lessonsCompleted++;
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
    
    public Integer getTotalXp() {
        return totalXp;
    }
    
    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getCurrentStreak() {
        return currentStreak;
    }
    
    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }
    
    public Integer getMaxStreak() {
        return maxStreak;
    }
    
    public void setMaxStreak(Integer maxStreak) {
        this.maxStreak = maxStreak;
    }
    
    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }
    
    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }
    
    public Integer getTasksCompleted() {
        return tasksCompleted;
    }
    
    public void setTasksCompleted(Integer tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }
    
    public Integer getLessonsCompleted() {
        return lessonsCompleted;
    }
    
    public void setLessonsCompleted(Integer lessonsCompleted) {
        this.lessonsCompleted = lessonsCompleted;
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


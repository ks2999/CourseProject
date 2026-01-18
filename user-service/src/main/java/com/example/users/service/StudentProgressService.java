package com.example.users.service;

import com.example.users.dto.StudentProgressResponse;
import com.example.users.model.StudentProgress;
import com.example.users.model.User;
import com.example.users.repository.StudentProgressRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentProgressService {
    
    private final StudentProgressRepository studentProgressRepository;
    private final UserRepository userRepository;
    
    public StudentProgressService(StudentProgressRepository studentProgressRepository,
                                 UserRepository userRepository) {
        this.studentProgressRepository = studentProgressRepository;
        this.userRepository = userRepository;
    }
    
    public StudentProgressResponse getProgress(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        StudentProgress progress = studentProgressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress newProgress = new StudentProgress(user);
                    return studentProgressRepository.save(newProgress);
                });
        
        return toResponse(progress);
    }
    
    public StudentProgressResponse addXp(UUID userId, int xp) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        StudentProgress progress = studentProgressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress newProgress = new StudentProgress(user);
                    return studentProgressRepository.save(newProgress);
                });
        
        progress.addXp(xp);
        progress.updateStreak();
        progress = studentProgressRepository.save(progress);
        
        return toResponse(progress);
    }
    
    public StudentProgressResponse incrementTasksCompleted(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        StudentProgress progress = studentProgressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress newProgress = new StudentProgress(user);
                    return studentProgressRepository.save(newProgress);
                });
        
        progress.incrementTasksCompleted();
        progress.updateStreak();
        progress = studentProgressRepository.save(progress);
        
        return toResponse(progress);
    }
    
    public StudentProgressResponse incrementLessonsCompleted(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        StudentProgress progress = studentProgressRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProgress newProgress = new StudentProgress(user);
                    return studentProgressRepository.save(newProgress);
                });
        
        progress.incrementLessonsCompleted();
        progress.updateStreak();
        progress = studentProgressRepository.save(progress);
        
        return toResponse(progress);
    }
    
    public List<StudentProgressResponse> getLeaderboard() {
        return studentProgressRepository.findAllOrderByTotalXpDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<StudentProgressResponse> getLeaderboardByLevel() {
        return studentProgressRepository.findAllOrderByLevelDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    private StudentProgressResponse toResponse(StudentProgress progress) {
        StudentProgressResponse response = new StudentProgressResponse();
        response.setId(progress.getId());
        response.setUserId(progress.getUser().getId());
        response.setUserName(progress.getUser().getName());
        response.setTotalXp(progress.getTotalXp());
        response.setLevel(progress.getLevel());
        response.setCurrentStreak(progress.getCurrentStreak());
        response.setMaxStreak(progress.getMaxStreak());
        response.setLastActivityDate(progress.getLastActivityDate());
        response.setTasksCompleted(progress.getTasksCompleted());
        response.setLessonsCompleted(progress.getLessonsCompleted());
        response.setCreatedAt(progress.getCreatedAt());
        response.setUpdatedAt(progress.getUpdatedAt());
        return response;
    }
}


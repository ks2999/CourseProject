package com.example.users.service;

import com.example.users.model.Achievement;
import com.example.users.repository.AchievementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AchievementService {
    
    private final AchievementRepository achievementRepository;
    
    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }
    
    public Achievement getAchievementById(UUID id) {
        return achievementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found with id: " + id));
    }
    
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
    
    public List<Achievement> getAchievementsByType(Achievement.Type type) {
        return achievementRepository.findByType(type);
    }
}


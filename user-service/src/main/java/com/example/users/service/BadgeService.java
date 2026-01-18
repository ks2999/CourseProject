package com.example.users.service;

import com.example.users.model.Achievement;
import com.example.users.model.Badge;
import com.example.users.model.User;
import com.example.users.repository.AchievementRepository;
import com.example.users.repository.BadgeRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BadgeService {
    
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final StudentProgressService studentProgressService;
    
    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       StudentProgressService studentProgressService) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.studentProgressService = studentProgressService;
    }
    
    public Badge awardBadge(UUID userId, UUID achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found"));
        
        if (badgeRepository.existsByUserAndAchievement(user, achievement)) {
            throw new IllegalArgumentException("User already has this badge");
        }
        
        Badge badge = new Badge(user, achievement);
        badge = badgeRepository.save(badge);
        
        // Начисляем опыт за достижение
        if (achievement.getXpReward() > 0) {
            studentProgressService.addXp(userId, achievement.getXpReward());
        }
        
        return badge;
    }
    
    public List<Badge> getUserBadges(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return badgeRepository.findByUser(user);
    }
    
    public boolean hasBadge(UUID userId, UUID achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found"));
        return badgeRepository.existsByUserAndAchievement(user, achievement);
    }
}


package com.example.users.repository;

import com.example.users.model.Achievement;
import com.example.users.model.Badge;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, UUID> {
    List<Badge> findByUser(User user);
    boolean existsByUserAndAchievement(User user, Achievement achievement);
}


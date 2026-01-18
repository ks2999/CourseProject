package com.example.users.repository;

import com.example.users.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    @Query("SELECT c FROM Challenge c WHERE c.active = true AND c.startDate <= :now AND c.endDate >= :now")
    List<Challenge> findActiveChallenges(LocalDateTime now);
    
    List<Challenge> findByActiveTrue();
}


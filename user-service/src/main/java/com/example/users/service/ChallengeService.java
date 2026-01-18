package com.example.users.service;

import com.example.users.model.Challenge;
import com.example.users.repository.ChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChallengeService {
    
    private final ChallengeRepository challengeRepository;
    
    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }
    
    public Challenge getChallengeById(UUID id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));
    }
    
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }
    
    public List<Challenge> getActiveChallenges() {
        return challengeRepository.findActiveChallenges(LocalDateTime.now());
    }
    
    public List<Challenge> getAllActiveChallenges() {
        return challengeRepository.findByActiveTrue();
    }
}


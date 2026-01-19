package com.example.users.service;

import com.example.users.dto.ChallengeRequest;
import com.example.users.dto.ChallengeResponse;
import com.example.users.model.Challenge;
import com.example.users.model.Task;
import com.example.users.model.User;
import com.example.users.repository.ChallengeRepository;
import com.example.users.repository.TaskRepository;
import com.example.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChallengeService {
    
    private static final Logger log = LoggerFactory.getLogger(ChallengeService.class);
    
    private final ChallengeRepository challengeRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    public ChallengeService(ChallengeRepository challengeRepository,
                           TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    
    public ChallengeResponse createChallenge(ChallengeRequest request, UUID createdById) {
        User creator = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createdById));
        
        // Проверяем права (только преподаватели и админы)
        if (!creator.getRole().isAdmin() && creator.getRole() != com.example.users.model.Role.TEACHER) {
            throw new IllegalArgumentException("Only teachers and admins can create challenges");
        }
        
        // Загружаем задачи
        List<Task> tasks = request.getTaskIds().stream()
                .map(taskId -> taskRepository.findById(taskId)
                        .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId)))
                .collect(Collectors.toList());
        
        Challenge challenge = new Challenge();
        challenge.setTitle(request.getTitle());
        challenge.setDescription(request.getDescription());
        challenge.setType(request.getType());
        challenge.setTasks(tasks);
        challenge.setStartDate(request.getStartDate());
        challenge.setEndDate(request.getEndDate());
        challenge.setXpReward(request.getXpReward() != null ? request.getXpReward() : 50);
        challenge.setActive(request.getActive() != null ? request.getActive() : true);
        challenge.setCreatedBy(creator);
        
        challenge = challengeRepository.save(challenge);
        log.info("Создано соревнование: {} с {} задачами", challenge.getTitle(), tasks.size());
        
        return toResponse(challenge);
    }
    
    public ChallengeResponse getChallengeById(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));
        return toResponse(challenge);
    }
    
    public List<ChallengeResponse> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<ChallengeResponse> getActiveChallenges() {
        return challengeRepository.findActiveChallenges(LocalDateTime.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<ChallengeResponse> getAllActiveChallenges() {
        return challengeRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public ChallengeResponse updateChallenge(UUID id, ChallengeRequest request, UUID currentUserId) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));
        
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Проверяем права
        if (!currentUser.getRole().isAdmin() && 
            (challenge.getCreatedBy() == null || !challenge.getCreatedBy().getId().equals(currentUserId))) {
            throw new IllegalArgumentException("You don't have permission to update this challenge");
        }
        
        // Обновляем задачи
        if (request.getTaskIds() != null && !request.getTaskIds().isEmpty()) {
            List<Task> tasks = request.getTaskIds().stream()
                    .map(taskId -> taskRepository.findById(taskId)
                            .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId)))
                    .collect(Collectors.toList());
            challenge.setTasks(tasks);
        }
        
        if (request.getTitle() != null) {
            challenge.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            challenge.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            challenge.setType(request.getType());
        }
        if (request.getStartDate() != null) {
            challenge.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            challenge.setEndDate(request.getEndDate());
        }
        if (request.getXpReward() != null) {
            challenge.setXpReward(request.getXpReward());
        }
        if (request.getActive() != null) {
            challenge.setActive(request.getActive());
        }
        
        challenge = challengeRepository.save(challenge);
        return toResponse(challenge);
    }
    
    public void deleteChallenge(UUID id, UUID currentUserId) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));
        
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Проверяем права
        if (!currentUser.getRole().isAdmin() && 
            (challenge.getCreatedBy() == null || !challenge.getCreatedBy().getId().equals(currentUserId))) {
            throw new IllegalArgumentException("You don't have permission to delete this challenge");
        }
        
        challengeRepository.delete(challenge);
    }
    
    private ChallengeResponse toResponse(Challenge challenge) {
        ChallengeResponse response = new ChallengeResponse();
        response.setId(challenge.getId());
        response.setTitle(challenge.getTitle());
        response.setDescription(challenge.getDescription());
        response.setType(challenge.getType().name());
        response.setTypeDescription(challenge.getType().getDescription());
        response.setStartDate(challenge.getStartDate());
        response.setEndDate(challenge.getEndDate());
        response.setXpReward(challenge.getXpReward());
        response.setActive(challenge.getActive());
        response.setIsCurrentlyActive(challenge.isActive());
        response.setCreatedAt(challenge.getCreatedAt());
        response.setUpdatedAt(challenge.getUpdatedAt());
        
        // Задачи
        if (challenge.getTasks() != null) {
            List<ChallengeResponse.TaskInfo> taskInfos = challenge.getTasks().stream()
                    .map(task -> new ChallengeResponse.TaskInfo(
                            task.getId(),
                            task.getTitle(),
                            task.getDifficulty().name(),
                            task.getXpReward()
                    ))
                    .collect(Collectors.toList());
            response.setTasks(taskInfos);
        }
        
        // Создатель
        if (challenge.getCreatedBy() != null) {
            response.setCreatedById(challenge.getCreatedBy().getId());
            response.setCreatedByName(challenge.getCreatedBy().getName());
        }
        
        return response;
    }
}

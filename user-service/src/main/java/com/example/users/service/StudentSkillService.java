package com.example.users.service;

import com.example.users.dto.StudentSkillResponse;
import com.example.users.model.Skill;
import com.example.users.model.StudentSkill;
import com.example.users.model.User;
import com.example.users.repository.SkillRepository;
import com.example.users.repository.StudentSkillRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentSkillService {
    
    private final StudentSkillRepository studentSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    
    public StudentSkillService(StudentSkillRepository studentSkillRepository,
                              UserRepository userRepository,
                              SkillRepository skillRepository) {
        this.studentSkillRepository = studentSkillRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }
    
    public StudentSkillResponse addExperienceToSkill(UUID userId, UUID skillId, int experience) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));
        
        Optional<StudentSkill> existing = studentSkillRepository.findByUserAndSkill(user, skill);
        StudentSkill studentSkill;
        
        if (existing.isPresent()) {
            studentSkill = existing.get();
        } else {
            studentSkill = new StudentSkill(user, skill);
        }
        
        studentSkill.addExperience(experience);
        studentSkill = studentSkillRepository.save(studentSkill);
        
        return toResponse(studentSkill);
    }
    
    public List<StudentSkillResponse> getStudentSkills(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        return studentSkillRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public StudentSkillResponse getStudentSkill(UUID userId, UUID skillId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));
        
        return studentSkillRepository.findByUserAndSkill(user, skill)
                .map(this::toResponse)
                .orElse(null);
    }
    
    private StudentSkillResponse toResponse(StudentSkill studentSkill) {
        StudentSkillResponse response = new StudentSkillResponse();
        response.setId(studentSkill.getId());
        response.setUserId(studentSkill.getUser().getId());
        response.setSkillId(studentSkill.getSkill().getId());
        response.setSkillName(studentSkill.getSkill().getName());
        response.setCurrentLevel(studentSkill.getCurrentLevel());
        response.setExperience(studentSkill.getExperience());
        response.setMaxLevel(studentSkill.getSkill().getMaxLevel());
        response.setCreatedAt(studentSkill.getCreatedAt());
        response.setUpdatedAt(studentSkill.getUpdatedAt());
        return response;
    }
}


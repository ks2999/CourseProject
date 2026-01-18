package com.example.users.service;

import com.example.users.dto.SkillRequest;
import com.example.users.dto.SkillResponse;
import com.example.users.model.Skill;
import com.example.users.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillService {
    
    private final SkillRepository skillRepository;
    
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    
    public SkillResponse createSkill(SkillRequest request) {
        if (skillRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Skill with name " + request.getName() + " already exists");
        }
        
        Skill skill = new Skill();
        skill.setName(request.getName());
        skill.setDescription(request.getDescription());
        skill.setMaxLevel(request.getMaxLevel() != null ? request.getMaxLevel() : 10);
        
        skill = skillRepository.save(skill);
        return toResponse(skill);
    }
    
    public SkillResponse getSkillById(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));
        return toResponse(skill);
    }
    
    public SkillResponse getSkillByName(String name) {
        Skill skill = skillRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with name: " + name));
        return toResponse(skill);
    }
    
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public SkillResponse updateSkill(UUID id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));
        
        if (request.getName() != null && !request.getName().equals(skill.getName())) {
            if (skillRepository.findByName(request.getName()).isPresent()) {
                throw new IllegalArgumentException("Skill with name " + request.getName() + " already exists");
            }
            skill.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            skill.setDescription(request.getDescription());
        }
        
        if (request.getMaxLevel() != null) {
            skill.setMaxLevel(request.getMaxLevel());
        }
        
        skill = skillRepository.save(skill);
        return toResponse(skill);
    }
    
    public void deleteSkill(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));
        skillRepository.delete(skill);
    }
    
    private SkillResponse toResponse(Skill skill) {
        SkillResponse response = new SkillResponse();
        response.setId(skill.getId());
        response.setName(skill.getName());
        response.setDescription(skill.getDescription());
        response.setMaxLevel(skill.getMaxLevel());
        response.setCreatedAt(skill.getCreatedAt());
        response.setUpdatedAt(skill.getUpdatedAt());
        return response;
    }
}


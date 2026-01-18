package com.example.users.service;

import com.example.users.dto.LessonRequest;
import com.example.users.dto.LessonResponse;
import com.example.users.model.Lesson;
import com.example.users.model.User;
import com.example.users.repository.LessonRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    
    public LessonService(LessonRepository lessonRepository, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }
    
    public LessonResponse createLesson(LessonRequest request, UUID createdById) {
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createdById));
        
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setContent(request.getContent());
        lesson.setTopic(request.getTopic());
        lesson.setOrderNumber(request.getOrderNumber());
        lesson.setPublished(request.getPublished() != null ? request.getPublished() : false);
        lesson.setCreatedBy(createdBy);
        
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }
    
    public LessonResponse getLessonById(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));
        return toResponse(lesson);
    }
    
    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<LessonResponse> getPublishedLessons() {
        return lessonRepository.findByPublishedTrueOrderByOrderNumberAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<LessonResponse> getLessonsByTopic(String topic) {
        return lessonRepository.findByTopic(topic).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<LessonResponse> getLessonsByCreator(UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + creatorId));
        return lessonRepository.findByCreatedBy(creator).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public LessonResponse updateLesson(UUID id, LessonRequest request, UUID currentUserId) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));
        
        // Проверка прав: только создатель или админ может редактировать
        if (!lesson.getCreatedBy().getId().equals(currentUserId)) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!currentUser.canWriteUsers()) {
                throw new SecurityException("You don't have permission to edit this lesson");
            }
        }
        
        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            lesson.setDescription(request.getDescription());
        }
        if (request.getContent() != null) {
            lesson.setContent(request.getContent());
        }
        if (request.getTopic() != null) {
            lesson.setTopic(request.getTopic());
        }
        if (request.getOrderNumber() != null) {
            lesson.setOrderNumber(request.getOrderNumber());
        }
        if (request.getPublished() != null) {
            lesson.setPublished(request.getPublished());
        }
        
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }
    
    public void deleteLesson(UUID id, UUID currentUserId) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));
        
        // Проверка прав
        if (!lesson.getCreatedBy().getId().equals(currentUserId)) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!currentUser.canDeleteUsers()) {
                throw new SecurityException("You don't have permission to delete this lesson");
            }
        }
        
        lessonRepository.delete(lesson);
    }
    
    private LessonResponse toResponse(Lesson lesson) {
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTitle(lesson.getTitle());
        response.setDescription(lesson.getDescription());
        response.setContent(lesson.getContent());
        response.setTopic(lesson.getTopic());
        response.setOrderNumber(lesson.getOrderNumber());
        response.setPublished(lesson.getPublished());
        response.setCreatedAt(lesson.getCreatedAt());
        response.setUpdatedAt(lesson.getUpdatedAt());
        
        if (lesson.getCreatedBy() != null) {
            response.setCreatedById(lesson.getCreatedBy().getId());
            response.setCreatedByName(lesson.getCreatedBy().getName());
        }
        
        return response;
    }
}


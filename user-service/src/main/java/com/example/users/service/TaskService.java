package com.example.users.service;

import com.example.users.dto.TaskRequest;
import com.example.users.dto.TaskResponse;
import com.example.users.model.Lesson;
import com.example.users.model.Skill;
import com.example.users.model.Task;
import com.example.users.model.User;
import com.example.users.repository.LessonRepository;
import com.example.users.repository.SkillRepository;
import com.example.users.repository.TaskRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final SkillRepository skillRepository;
    
    public TaskService(TaskRepository taskRepository, UserRepository userRepository,
                      LessonRepository lessonRepository, SkillRepository skillRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.skillRepository = skillRepository;
    }
    
    public TaskResponse createTask(TaskRequest request, UUID createdById) {
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createdById));
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCodeTemplate(request.getCodeTemplate());
        task.setTestCases(request.getTestCases());
        task.setXpReward(request.getXpReward() != null ? request.getXpReward() : 10);
        task.setDifficulty(request.getDifficulty());
        task.setPublished(request.getPublished() != null ? request.getPublished() : false);
        task.setCreatedBy(createdBy);
        
        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + request.getLessonId()));
            task.setLesson(lesson);
        }
        
        if (request.getSkillId() != null) {
            Skill skill = skillRepository.findById(request.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + request.getSkillId()));
            task.setSkill(skill);
        }
        
        task = taskRepository.save(task);
        return toResponse(task);
    }
    
    public TaskResponse getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        return toResponse(task);
    }
    
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<TaskResponse> getPublishedTasks() {
        return taskRepository.findByPublishedTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<TaskResponse> getTasksByLesson(UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        return taskRepository.findByLessonAndPublishedTrue(lesson).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public TaskResponse updateTask(UUID id, TaskRequest request, UUID currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        
        // Проверка прав
        if (!task.getCreatedBy().getId().equals(currentUserId)) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!currentUser.canWriteUsers()) {
                throw new SecurityException("You don't have permission to edit this task");
            }
        }
        
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getCodeTemplate() != null) {
            task.setCodeTemplate(request.getCodeTemplate());
        }
        if (request.getTestCases() != null) {
            task.setTestCases(request.getTestCases());
        }
        if (request.getXpReward() != null) {
            task.setXpReward(request.getXpReward());
        }
        if (request.getDifficulty() != null) {
            task.setDifficulty(request.getDifficulty());
        }
        if (request.getPublished() != null) {
            task.setPublished(request.getPublished());
        }
        
        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId())
                    .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
            task.setLesson(lesson);
        }
        
        if (request.getSkillId() != null) {
            Skill skill = skillRepository.findById(request.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found"));
            task.setSkill(skill);
        }
        
        task = taskRepository.save(task);
        return toResponse(task);
    }
    
    public void deleteTask(UUID id, UUID currentUserId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        
        // Проверка прав
        if (!task.getCreatedBy().getId().equals(currentUserId)) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!currentUser.canDeleteUsers()) {
                throw new SecurityException("You don't have permission to delete this task");
            }
        }
        
        taskRepository.delete(task);
    }
    
    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCodeTemplate(task.getCodeTemplate());
        response.setXpReward(task.getXpReward());
        response.setDifficulty(task.getDifficulty());
        response.setPublished(task.getPublished());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        
        if (task.getCreatedBy() != null) {
            response.setCreatedById(task.getCreatedBy().getId());
            response.setCreatedByName(task.getCreatedBy().getName());
        }
        
        if (task.getLesson() != null) {
            response.setLessonId(task.getLesson().getId());
            response.setLessonTitle(task.getLesson().getTitle());
        }
        
        if (task.getSkill() != null) {
            response.setSkillId(task.getSkill().getId());
            response.setSkillName(task.getSkill().getName());
        }
        
        return response;
    }
}


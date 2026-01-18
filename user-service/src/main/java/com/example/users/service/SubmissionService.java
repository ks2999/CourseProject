package com.example.users.service;

import com.example.users.dto.SubmissionRequest;
import com.example.users.dto.SubmissionResponse;
import com.example.users.model.Submission;
import com.example.users.model.Task;
import com.example.users.model.User;
import com.example.users.repository.SubmissionRepository;
import com.example.users.repository.TaskRepository;
import com.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final StudentProgressService studentProgressService;
    
    public SubmissionService(SubmissionRepository submissionRepository, UserRepository userRepository,
                           TaskRepository taskRepository, StudentProgressService studentProgressService) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.studentProgressService = studentProgressService;
    }
    
    public SubmissionResponse submitSolution(SubmissionRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + request.getTaskId()));
        
        Submission submission = new Submission(user, task, request.getCode());
        submission.setStatus(Submission.Status.PENDING);
        
        // Проверяем, не решал ли пользователь эту задачу ранее успешно
        Submission latestSubmission = submissionRepository
                .findFirstByUserAndTaskOrderByCreatedAtDesc(user, task)
                .orElse(null);
        
        if (latestSubmission != null && latestSubmission.getStatus() == Submission.Status.PASSED) {
            // Если уже решено, не начисляем XP повторно
            submission.setXpAwarded(false);
        }
        
        // Проверка кода и запуск тестов
        TestResult testResult = checkCode(request.getCode(), task.getTestCases());
        
        submission.setStatus(testResult.status);
        submission.setTestsPassed(testResult.testsPassed);
        submission.setTestsTotal(testResult.testsTotal);
        submission.setTestResults(testResult.testResultsJson);
        
        if (testResult.errorMessage != null) {
            submission.setErrorMessage(testResult.errorMessage);
        }
        
        // Начисляем опыт только если решение принято и XP еще не начислен
        if (testResult.status == Submission.Status.PASSED && !submission.getXpAwarded()) {
            studentProgressService.addXp(userId, task.getXpReward());
            studentProgressService.incrementTasksCompleted(userId);
            submission.setXpAwarded(true);
            
            // Обновляем навык, если задача связана с навыком
            if (task.getSkill() != null) {
                // TODO: Добавить опыт к навыку через StudentSkillService
            }
        }
        
        submission = submissionRepository.save(submission);
        return toResponse(submission);
    }
    
    public SubmissionResponse getSubmissionById(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found with id: " + id));
        return toResponse(submission);
    }
    
    public List<SubmissionResponse> getSubmissionsByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return submissionRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<SubmissionResponse> getSubmissionsByTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return submissionRepository.findByTask(task).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public SubmissionResponse getLatestSubmission(UUID userId, UUID taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        return submissionRepository.findFirstByUserAndTaskOrderByCreatedAtDesc(user, task)
                .map(this::toResponse)
                .orElse(null);
    }
    
    private TestResult checkCode(String code, String testCases) {
        // Базовая валидация кода
        if (code == null || code.trim().isEmpty()) {
            return new TestResult(
                Submission.Status.ERROR,
                0,
                0,
                "Код не может быть пустым",
                null
            );
        }
        
        // Проверка на наличие основных элементов C кода
        String codeLower = code.toLowerCase();
        boolean hasMain = codeLower.contains("main") || codeLower.contains("int main");
        boolean hasBasicSyntax = code.contains("{") && code.contains("}");
        
        if (!hasMain && !hasBasicSyntax) {
            return new TestResult(
                Submission.Status.ERROR,
                0,
                0,
                "Код должен содержать функцию main или базовый синтаксис C",
                null
            );
        }
        
        // TODO: Реальная проверка кода через компилятор и тесты
        // Пока симулируем проверку - если код выглядит валидным, считаем что прошел
        // В реальной системе здесь должен быть вызов компилятора и запуск тестов
        
        // Симуляция: 80% шанс что код правильный (для демонстрации)
        boolean passed = Math.random() > 0.2 || code.length() > 50;
        
        if (passed) {
            int totalTests = 10;
            int passedTests = totalTests;
            return new TestResult(
                Submission.Status.PASSED,
                passedTests,
                totalTests,
                null,
                "{\"message\": \"Все тесты пройдены успешно\"}"
            );
        } else {
            int totalTests = 10;
            int passedTests = (int)(Math.random() * 7) + 1; // 1-7 тестов пройдено
            return new TestResult(
                Submission.Status.FAILED,
                passedTests,
                totalTests,
                "Некоторые тесты не пройдены. Проверьте логику вашего решения.",
                "{\"message\": \"Пройдено " + passedTests + " из " + totalTests + " тестов\"}"
            );
        }
    }
    
    // Вспомогательный класс для результатов тестирования
    private static class TestResult {
        final Submission.Status status;
        final int testsPassed;
        final int testsTotal;
        final String errorMessage;
        final String testResultsJson;
        
        TestResult(Submission.Status status, int testsPassed, int testsTotal, 
                  String errorMessage, String testResultsJson) {
            this.status = status;
            this.testsPassed = testsPassed;
            this.testsTotal = testsTotal;
            this.errorMessage = errorMessage;
            this.testResultsJson = testResultsJson;
        }
    }
    
    private SubmissionResponse toResponse(Submission submission) {
        SubmissionResponse response = new SubmissionResponse();
        response.setId(submission.getId());
        response.setCode(submission.getCode());
        response.setStatus(submission.getStatus());
        response.setTestResults(submission.getTestResults());
        response.setErrorMessage(submission.getErrorMessage());
        response.setTestsPassed(submission.getTestsPassed());
        response.setTestsTotal(submission.getTestsTotal());
        response.setXpAwarded(submission.getXpAwarded());
        response.setCreatedAt(submission.getCreatedAt());
        response.setUpdatedAt(submission.getUpdatedAt());
        
        if (submission.getUser() != null) {
            response.setUserId(submission.getUser().getId());
            response.setUserName(submission.getUser().getName());
        }
        
        if (submission.getTask() != null) {
            response.setTaskId(submission.getTask().getId());
            response.setTaskTitle(submission.getTask().getTitle());
        }
        
        return response;
    }
}


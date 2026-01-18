package com.example.users.service;

import com.example.users.dto.SubmissionRequest;
import com.example.users.dto.SubmissionResponse;
import com.example.users.model.Submission;
import com.example.users.model.Task;
import com.example.users.model.User;
import com.example.users.repository.SubmissionRepository;
import com.example.users.repository.TaskRepository;
import com.example.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubmissionService {
    
    private static final Logger log = LoggerFactory.getLogger(SubmissionService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final StudentProgressService studentProgressService;
    private final CodeExecutionService codeExecutionService;
    
    public SubmissionService(SubmissionRepository submissionRepository, UserRepository userRepository,
                           TaskRepository taskRepository, StudentProgressService studentProgressService,
                           CodeExecutionService codeExecutionService) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.studentProgressService = studentProgressService;
        this.codeExecutionService = codeExecutionService;
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
        
        log.info("Проверка кода завершена: статус={}, тестов пройдено={}/{}", 
                testResult.status, testResult.testsPassed, testResult.testsTotal);
        
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
        
        // Проверяем наличие GCC компилятора
        if (!isGccAvailable()) {
            log.error("GCC не найден! Установите GCC для проверки кода.");
            return new TestResult(
                Submission.Status.ERROR,
                0,
                0,
                "GCC компилятор не найден. Установите GCC для проверки кода:\n" +
                "macOS: brew install gcc\n" +
                "Linux: sudo apt-get install gcc",
                null
            );
        }
        
        try {
            // Реальная проверка кода через компилятор
            CodeExecutionService.CodeCheckResult result = 
                codeExecutionService.checkCode(code, testCases);
            
            if (!result.compilationSuccess) {
                // Fallback: показываем хотя бы один тест с ошибкой компиляции
                try {
                    List<FallbackTestCase> fallbackTests = parseTestCasesForFallback(testCases);
                    if (!fallbackTests.isEmpty()) {
                        FallbackTestCase firstTest = fallbackTests.get(0);
                        String errorJson = String.format(
                            "{\"tests\":[{\"testNumber\":1,\"passed\":false,\"input\":\"%s\",\"expected\":\"%s\",\"actual\":\"\",\"error\":\"Ошибка компиляции:\\n%s\"}]}",
                            escapeJson(firstTest.input),
                            escapeJson(firstTest.expectedOutput),
                            escapeJson(result.compilationError)
                        );
                        
                        return new TestResult(
                            Submission.Status.ERROR,
                            0,
                            1,
                            "Ошибка компиляции:\n" + result.compilationError,
                            errorJson
                        );
                    }
                } catch (Exception e) {
                    log.error("Ошибка при создании fallback для ошибки компиляции", e);
                }
                
                return new TestResult(
                    Submission.Status.ERROR,
                    0,
                    0,
                    "Ошибка компиляции:\n" + result.compilationError,
                    null
                );
            }
            
            if (result.testsTotal == 0) {
                // Fallback: показываем хотя бы один тест
                try {
                    List<FallbackTestCase> fallbackTests = parseTestCasesForFallback(testCases);
                    if (!fallbackTests.isEmpty()) {
                        FallbackTestCase firstTest = fallbackTests.get(0);
                        String errorJson = String.format(
                            "{\"tests\":[{\"testNumber\":1,\"passed\":false,\"input\":\"%s\",\"expected\":\"%s\",\"actual\":\"\",\"error\":\"Тестовые случаи не найдены или не удалось их распарсить\"}]}",
                            escapeJson(firstTest.input),
                            escapeJson(firstTest.expectedOutput)
                        );
                        
                        return new TestResult(
                            Submission.Status.ERROR,
                            0,
                            1,
                            "Тестовые случаи не найдены",
                            errorJson
                        );
                    }
                } catch (Exception e) {
                    log.error("Ошибка при создании fallback", e);
                }
                
                return new TestResult(
                    Submission.Status.ERROR,
                    0,
                    0,
                    "Тестовые случаи не найдены",
                    null
                );
            }
            
            // Формируем JSON с результатами тестов
            String testResultsJson = buildTestResultsJson(result.testResults);
            
            Submission.Status status = result.testsPassed == result.testsTotal 
                ? Submission.Status.PASSED 
                : Submission.Status.FAILED;
            
            String errorMessage = status == Submission.Status.FAILED
                ? String.format("Пройдено %d из %d тестов", result.testsPassed, result.testsTotal)
                : null;
            
            return new TestResult(
                status,
                result.testsPassed,
                result.testsTotal,
                errorMessage,
                testResultsJson
            );
            
        } catch (Exception e) {
            log.error("Ошибка при проверке кода", e);
            
            // Fallback: показываем хотя бы один тест с информацией об ошибке
            try {
                List<FallbackTestCase> fallbackTests = parseTestCasesForFallback(testCases);
                if (!fallbackTests.isEmpty()) {
                    FallbackTestCase firstTest = fallbackTests.get(0);
                    String errorJson = String.format(
                        "{\"tests\":[{\"testNumber\":1,\"passed\":false,\"input\":\"%s\",\"expected\":\"%s\",\"actual\":\"\",\"error\":\"Ошибка системы: %s\"}]}",
                        escapeJson(firstTest.input),
                        escapeJson(firstTest.expectedOutput),
                        escapeJson(e.getMessage())
                    );
                    
                    return new TestResult(
                        Submission.Status.ERROR,
                        0,
                        1,
                        "Ошибка системы при проверке кода: " + e.getMessage(),
                        errorJson
                    );
                }
            } catch (Exception fallbackError) {
                log.error("Ошибка при создании fallback результата", fallbackError);
            }
            
            return new TestResult(
                Submission.Status.ERROR,
                0,
                0,
                "Ошибка системы при проверке кода: " + e.getMessage(),
                null
            );
        }
    }
    
    /**
     * Проверяет доступность GCC компилятора
     */
    private boolean isGccAvailable() {
        try {
            Process process = new ProcessBuilder("gcc", "--version").start();
            boolean finished = process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                return true;
            }
        } catch (Exception e) {
            // GCC не найден
        }
        return false;
    }
    
    /**
     * Парсит тестовые случаи для fallback (простая версия)
     */
    private List<FallbackTestCase> parseTestCasesForFallback(String testCasesJson) {
        List<FallbackTestCase> testCases = new ArrayList<>();
        
        try {
            com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(testCasesJson);
            com.fasterxml.jackson.databind.JsonNode tests = root.get("tests");
            
            if (tests != null && tests.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode test : tests) {
                    String input = test.has("input") ? test.get("input").asText() : "";
                    String output = test.has("output") ? test.get("output").asText() : "";
                    testCases.add(new FallbackTestCase(input, output));
                }
            }
        } catch (Exception e) {
            log.warn("Не удалось распарсить тестовые случаи для fallback", e);
        }
        
        return testCases;
    }
    
    private static class FallbackTestCase {
        final String input;
        final String expectedOutput;
        
        FallbackTestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }
    }
    
    /**
     * Формирует JSON с результатами тестов
     */
    private String buildTestResultsJson(List<CodeExecutionService.TestExecutionResult> testResults) {
        try {
            StringBuilder json = new StringBuilder("{\"tests\":[");
            for (int i = 0; i < testResults.size(); i++) {
                CodeExecutionService.TestExecutionResult tr = testResults.get(i);
                if (i > 0) json.append(",");
                json.append("{")
                    .append("\"testNumber\":").append(i + 1).append(",")
                    .append("\"passed\":").append(tr.passed).append(",")
                    .append("\"input\":\"").append(escapeJson(tr.input)).append("\",")
                    .append("\"expected\":\"").append(escapeJson(tr.expectedOutput)).append("\",")
                    .append("\"actual\":\"").append(escapeJson(tr.actualOutput)).append("\"");
                if (tr.errorMessage != null) {
                    json.append(",\"error\":\"").append(escapeJson(tr.errorMessage)).append("\"");
                }
                json.append("}");
            }
            json.append("]}");
            return json.toString();
        } catch (Exception e) {
            log.error("Ошибка формирования JSON результатов", e);
            return "{\"error\":\"Ошибка формирования результатов\"}";
        }
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
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


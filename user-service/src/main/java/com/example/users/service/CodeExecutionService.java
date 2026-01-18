package com.example.users.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Сервис для компиляции и выполнения C кода
 */
@Service
public class CodeExecutionService {
    
    private static final Logger log = LoggerFactory.getLogger(CodeExecutionService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final int TIMEOUT_SECONDS = 5; // Таймаут выполнения программы
    private static final int MAX_OUTPUT_SIZE = 10000; // Максимальный размер вывода
    
    /**
     * Результат выполнения теста
     */
    public static class TestExecutionResult {
        public final boolean passed;
        public final String input;
        public final String expectedOutput;
        public final String actualOutput;
        public final String errorMessage;
        
        public TestExecutionResult(boolean passed, String input, String expectedOutput, 
                                  String actualOutput, String errorMessage) {
            this.passed = passed;
            this.input = input;
            this.expectedOutput = expectedOutput;
            this.actualOutput = actualOutput;
            this.errorMessage = errorMessage;
        }
    }
    
    /**
     * Результат проверки кода
     */
    public static class CodeCheckResult {
        public final boolean compilationSuccess;
        public final String compilationError;
        public final List<TestExecutionResult> testResults;
        public final int testsPassed;
        public final int testsTotal;
        
        public CodeCheckResult(boolean compilationSuccess, String compilationError,
                              List<TestExecutionResult> testResults) {
            this.compilationSuccess = compilationSuccess;
            this.compilationError = compilationError;
            this.testResults = testResults;
            this.testsTotal = testResults.size();
            this.testsPassed = (int) testResults.stream().filter(t -> t.passed).count();
        }
    }
    
    /**
     * Компилирует и проверяет C код на тестовых данных
     */
    public CodeCheckResult checkCode(String code, String testCasesJson) {
        String uniqueId = UUID.randomUUID().toString();
        Path workDir = Paths.get(TEMP_DIR, "code_exec_" + uniqueId);
        
        try {
            // Создаем рабочую директорию
            Files.createDirectories(workDir);
            
            // Сохраняем код в файл
            Path sourceFile = workDir.resolve("solution.c");
            Files.write(sourceFile, code.getBytes());
            
            // Компилируем код
            CompilationResult compilation = compileCode(sourceFile, workDir);
            if (!compilation.success) {
                return new CodeCheckResult(false, compilation.error, new ArrayList<>());
            }
            
            // Парсим тестовые случаи
            List<TestCase> testCases = parseTestCases(testCasesJson);
            
            if (testCases.isEmpty()) {
                log.warn("Тестовые случаи не найдены или пусты");
                return new CodeCheckResult(false, "Тестовые случаи не найдены", new ArrayList<>());
            }
            
            log.info("Найдено {} тестовых случаев", testCases.size());
            
            // Запускаем тесты
            List<TestExecutionResult> testResults = new ArrayList<>();
            for (int i = 0; i < testCases.size(); i++) {
                TestCase testCase = testCases.get(i);
                log.debug("Запуск теста {} из {}", i + 1, testCases.size());
                TestExecutionResult result = runTest(workDir.resolve("solution"), testCase);
                testResults.add(result);
            }
            
            int passed = (int) testResults.stream().filter(t -> t.passed).count();
            log.info("Пройдено тестов: {} из {}", passed, testResults.size());
            
            return new CodeCheckResult(true, null, testResults);
            
        } catch (Exception e) {
            log.error("Ошибка при проверке кода", e);
            return new CodeCheckResult(false, "Ошибка системы: " + e.getMessage(), new ArrayList<>());
        } finally {
            // Удаляем временные файлы
            cleanup(workDir);
        }
    }
    
    /**
     * Компилирует C код
     */
    private CompilationResult compileCode(Path sourceFile, Path workDir) {
        try {
            log.debug("Компиляция файла: {}", sourceFile);
            ProcessBuilder pb = new ProcessBuilder("gcc", "-o", "solution", "solution.c", 
                                                   "-std=c11", "-Wall", "-Wextra", "-O2");
            pb.directory(workDir.toFile());
            pb.redirectErrorStream(false);
            
            Process process = pb.start();
            
            // Читаем ошибки компиляции из stderr
            StringBuilder errorOutput = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    if (errorOutput.length() > 0) {
                        errorOutput.append("\n");
                    }
                    errorOutput.append(line);
                }
            }
            
            // Также читаем stdout на случай если там есть предупреждения
            try (BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdoutReader.readLine()) != null) {
                    if (errorOutput.length() > 0) {
                        errorOutput.append("\n");
                    }
                    errorOutput.append(line);
                }
            }
            
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.warn("Таймаут компиляции");
                return new CompilationResult(false, "Таймаут компиляции (превышено 10 секунд)");
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                String error = errorOutput.toString();
                log.warn("Ошибка компиляции (код {}): {}", exitCode, error);
                return new CompilationResult(false, error.isEmpty() ? "Ошибка компиляции (код " + exitCode + ")" : error);
            }
            
            log.debug("Компиляция успешна");
            return new CompilationResult(true, null);
            
        } catch (IOException e) {
            log.error("IO ошибка при компиляции", e);
            return new CompilationResult(false, "Ошибка компиляции: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Компиляция прервана", e);
            return new CompilationResult(false, "Компиляция прервана");
        }
    }
    
    /**
     * Запускает программу с тестовыми данными
     */
    private TestExecutionResult runTest(Path executable, TestCase testCase) {
        StringBuilder errorOutput = new StringBuilder();
        
        try {
            if (!Files.exists(executable)) {
                log.error("Исполняемый файл не найден: {}", executable);
                return new TestExecutionResult(false, testCase.input, testCase.expectedOutput,
                    "", "Исполняемый файл не найден после компиляции");
            }
            
            log.debug("Запуск теста: input='{}', expected='{}'", testCase.input, testCase.expectedOutput);
            ProcessBuilder pb = new ProcessBuilder(executable.toString());
            pb.redirectErrorStream(false);
            
            Process process = pb.start();
            
            // Записываем входные данные
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(testCase.input);
                writer.flush();
                writer.close();
            }
            
            // Читаем стандартный вывод
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                int linesRead = 0;
                while ((line = reader.readLine()) != null && linesRead < 100) {
                    if (linesRead > 0) {
                        output.append("\n");
                    }
                    output.append(line);
                    linesRead++;
                }
            }
            
            // Читаем ошибки
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    if (errorOutput.length() > 0) {
                        errorOutput.append("\n");
                    }
                    errorOutput.append(line);
                }
            }
            
            // Ограничиваем размер вывода
            String actualOutput = output.toString();
            if (actualOutput.length() > MAX_OUTPUT_SIZE) {
                actualOutput = actualOutput.substring(0, MAX_OUTPUT_SIZE) + "... (обрезано)";
            }
            
            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new TestExecutionResult(false, testCase.input, testCase.expectedOutput,
                    actualOutput, "Таймаут выполнения (превышено " + TIMEOUT_SECONDS + " секунд)");
            }
            
            if (process.exitValue() != 0) {
                String errorMsg = "Программа завершилась с кодом " + process.exitValue();
                if (errorOutput.length() > 0) {
                    errorMsg += "\nОшибка: " + errorOutput.toString();
                }
                return new TestExecutionResult(false, testCase.input, testCase.expectedOutput,
                    actualOutput, errorMsg);
            }
            
            // Нормализуем вывод (убираем лишние пробелы в конце и начале)
            String normalizedActual = actualOutput.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
            String normalizedExpected = testCase.expectedOutput.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
            
            boolean passed = normalizedActual.equals(normalizedExpected);
            
            String errorMsg = null;
            if (!passed) {
                errorMsg = String.format("Ожидалось: '%s', получено: '%s'", 
                    normalizedExpected.isEmpty() ? "(пусто)" : normalizedExpected,
                    normalizedActual.isEmpty() ? "(пусто)" : normalizedActual);
            }
            
            return new TestExecutionResult(passed, testCase.input, normalizedExpected,
                normalizedActual, errorMsg);
            
        } catch (IOException e) {
            return new TestExecutionResult(false, testCase.input, testCase.expectedOutput,
                "", "Ошибка выполнения: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new TestExecutionResult(false, testCase.input, testCase.expectedOutput,
                "", "Выполнение прервано");
        }
    }
    
    /**
     * Парсит тестовые случаи из JSON
     */
    private List<TestCase> parseTestCases(String testCasesJson) {
        List<TestCase> testCases = new ArrayList<>();
        
        if (testCasesJson == null || testCasesJson.trim().isEmpty()) {
            log.warn("JSON с тестовыми случаями пуст");
            return testCases;
        }
        
        try {
            JsonNode root = objectMapper.readTree(testCasesJson);
            JsonNode tests = root.get("tests");
            
            if (tests != null && tests.isArray()) {
                for (JsonNode test : tests) {
                    String input = test.has("input") ? test.get("input").asText() : "";
                    String output = test.has("output") ? test.get("output").asText() : "";
                    testCases.add(new TestCase(input, output));
                }
                log.debug("Распарсено {} тестовых случаев", testCases.size());
            } else {
                log.warn("Тестовые случаи не найдены в JSON или не являются массивом");
            }
        } catch (Exception e) {
            log.error("Ошибка парсинга тестовых случаев: " + e.getMessage(), e);
            log.error("JSON содержимое: " + testCasesJson);
            // Возвращаем пустой список, если не удалось распарсить
        }
        
        return testCases;
    }
    
    /**
     * Удаляет временные файлы
     */
    private void cleanup(Path workDir) {
        try {
            if (Files.exists(workDir)) {
                Files.walk(workDir)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.warn("Не удалось удалить файл: " + path, e);
                        }
                    });
            }
        } catch (IOException e) {
            log.warn("Ошибка при очистке временных файлов", e);
        }
    }
    
    // Вспомогательные классы
    private static class CompilationResult {
        final boolean success;
        final String error;
        
        CompilationResult(boolean success, String error) {
            this.success = success;
            this.error = error;
        }
    }
    
    private static class TestCase {
        final String input;
        final String expectedOutput;
        
        TestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }
    }
}


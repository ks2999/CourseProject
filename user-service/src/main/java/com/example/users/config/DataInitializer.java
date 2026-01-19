package com.example.users.config;

import com.example.users.model.*;
import com.example.users.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Инициализация тестовых данных для профиля dev (H2)
 */
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;
    private final ChallengeRepository challengeRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository,
                          SkillRepository skillRepository,
                          LessonRepository lessonRepository,
                          TaskRepository taskRepository,
                          ChallengeRepository challengeRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.lessonRepository = lessonRepository;
        this.taskRepository = taskRepository;
        this.challengeRepository = challengeRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public void run(String... args) {
        log.info("Начало инициализации тестовых данных...");
        
        // Создаем тестового преподавателя
        User teacher = userRepository.findByEmail("teacher@example.com")
                .orElseGet(() -> {
                    log.info("Создание тестового преподавателя...");
                    User t = User.create("teacher@example.com", 
                            passwordEncoder.encode("password"), 
                            "Преподаватель", 
                            Role.TEACHER);
                    return userRepository.save(t);
                });
        log.info("Преподаватель готов: {}", teacher.getEmail());
        
        // Создаем навыки
        Skill skillLoops = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "Циклы",
            "Работа с циклами for, while, do-while"
        );
        
        Skill skillArrays = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            "Массивы",
            "Работа с одномерными и многомерными массивами"
        );
        
        Skill skillFunctions = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000003"),
            "Функции",
            "Создание и использование функций"
        );
        
        Skill skillPointers = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000004"),
            "Указатели",
            "Работа с указателями и динамической памятью"
        );
        
        Skill skillStrings = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000005"),
            "Строки",
            "Работа со строками в C"
        );
        
        Skill skillConditions = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000006"),
            "Условия",
            "Работа с условными операторами if, else, switch"
        );
        
        Skill skillStructs = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000007"),
            "Структуры",
            "Работа со структурами данных"
        );
        
        Skill skillFiles = createSkillIfNotExists(
            UUID.fromString("00000000-0000-0000-0000-000000000008"),
            "Файлы",
            "Работа с файлами в C"
        );
        
        // Создаем уроки с подробной теорией
        Lesson lesson1 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000001"),
            "Введение в циклы",
            "Изучите основы работы с циклами в языке C - от простых циклов до вложенных конструкций",
            LessonContent.LESSON_1_CYCLES,
            "Циклы",
            1,
            teacher
        );
        
        Lesson lesson2 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000002"),
            "Работа с массивами",
            "Изучите как работать с массивами в C - от одномерных до многомерных массивов",
            LessonContent.LESSON_2_ARRAYS,
            "Массивы",
            2,
            teacher
        );
        
        Lesson lesson3 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000003"),
            "Функции в C",
            "Научитесь создавать и использовать функции - от простых до рекурсивных",
            LessonContent.LESSON_3_FUNCTIONS,
            "Функции",
            3,
            teacher
        );
        
        Lesson lesson4 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000004"),
            "Указатели",
            "Изучите работу с указателями и динамической памятью - одна из самых мощных возможностей C",
            LessonContent.LESSON_4_POINTERS,
            "Указатели",
            4,
            teacher
        );
        
        Lesson lesson5 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000005"),
            "Работа со строками",
            "Научитесь работать со строками в C - от базовых операций до сложных алгоритмов",
            LessonContent.LESSON_5_STRINGS,
            "Строки",
            5,
            teacher
        );
        
        // Создаем дополнительные уроки
        Lesson lesson6 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000006"),
            "Условные операторы",
            "Изучите работу с условиями if, else, switch - основы принятия решений в программах",
            LessonContent.LESSON_6_CONDITIONS,
            "Условия",
            6,
            teacher
        );
        
        Lesson lesson7 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000007"),
            "Структуры данных",
            "Научитесь создавать и использовать структуры (struct) для организации данных",
            LessonContent.LESSON_7_STRUCTS,
            "Структуры",
            7,
            teacher
        );
        
        Lesson lesson8 = createLessonIfNotExists(
            UUID.fromString("10000000-0000-0000-0000-000000000008"),
            "Работа с файлами",
            "Изучите чтение и запись файлов в C - важный навык для работы с данными",
            LessonContent.LESSON_8_FILES,
            "Файлы",
            8,
            teacher
        );
        
        // Создаем задачи
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000001"),
            "Сумма чисел от 1 до N",
            "Напишите программу, которая вычисляет сумму всех чисел от 1 до N включительно.\n\nВходные данные: одно целое число N (1 ≤ N ≤ 1000)\nВыходные данные: сумма чисел от 1 до N\n\nПример:\nВход: 5\nВыход: 15 (1+2+3+4+5=15)",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\", \"output\": \"15\"}, {\"input\": \"10\", \"output\": \"55\"}, {\"input\": \"100\", \"output\": \"5050\"}]}",
            10,
            Task.Difficulty.EASY,
            lesson1,
            skillLoops,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000002"),
            "Факториал числа",
            "Вычислите факториал числа N. Факториал N (обозначается N!) - это произведение всех натуральных чисел от 1 до N.\n\nВходные данные: одно целое число N (0 ≤ N ≤ 10)\nВыходные данные: факториал числа N\n\nПример:\nВход: 5\nВыход: 120 (5! = 1*2*3*4*5 = 120)\n\nПримечание: 0! = 1",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\", \"output\": \"120\"}, {\"input\": \"0\", \"output\": \"1\"}, {\"input\": \"7\", \"output\": \"5040\"}]}",
            15,
            Task.Difficulty.EASY,
            lesson1,
            skillLoops,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000003"),
            "Максимальный элемент массива",
            "Найдите максимальный элемент в массиве из N целых чисел.\n\nВходные данные:\nПервая строка: N (1 ≤ N ≤ 1000) - размер массива\nВторая строка: N целых чисел через пробел\n\nВыходные данные: максимальный элемент\n\nПример:\nВход:\n5\n3 7 2 9 1\nВыход: 9",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    int arr[1000];\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\\n3 7 2 9 1\", \"output\": \"9\"}, {\"input\": \"3\\n-5 -2 -10\", \"output\": \"-2\"}]}",
            15,
            Task.Difficulty.EASY,
            lesson2,
            skillArrays,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000004"),
            "Сумма элементов массива",
            "Вычислите сумму всех элементов массива.\n\nВходные данные:\nПервая строка: N (1 ≤ N ≤ 1000) - размер массива\nВторая строка: N целых чисел через пробел\n\nВыходные данные: сумма всех элементов\n\nПример:\nВход:\n4\n1 2 3 4\nВыход: 10",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    int arr[1000];\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"4\\n1 2 3 4\", \"output\": \"10\"}, {\"input\": \"3\\n-5 10 -3\", \"output\": \"2\"}]}",
            10,
            Task.Difficulty.EASY,
            lesson2,
            skillArrays,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000005"),
            "Функция для вычисления степени",
            "Напишите функцию power(int base, int exponent), которая вычисляет base в степени exponent.\n\nВходные данные: два целых числа base и exponent (0 ≤ exponent ≤ 10)\nВыходные данные: base^exponent\n\nПример:\nВход: 2 3\nВыход: 8 (2^3 = 8)\n\nПримечание: base^0 = 1 для любого base",
            "#include <stdio.h>\n\n// Ваша функция здесь\n\nint main() {\n    int base, exponent;\n    scanf(\"%d %d\", &base, &exponent);\n    \n    // Вызовите вашу функцию и выведите результат\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"2 3\", \"output\": \"8\"}, {\"input\": \"5 0\", \"output\": \"1\"}, {\"input\": \"3 4\", \"output\": \"81\"}]}",
            20,
            Task.Difficulty.MEDIUM,
            lesson3,
            skillFunctions,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000006"),
            "Проверка на простое число",
            "Напишите функцию isPrime(int n), которая проверяет, является ли число простым.\n\nПростое число - это число, которое делится только на 1 и на само себя.\n\nВходные данные: одно целое число N (2 ≤ N ≤ 1000)\nВыходные данные: 1 если число простое, 0 если составное\n\nПример:\nВход: 7\nВыход: 1\n\nВход: 10\nВыход: 0",
            "#include <stdio.h>\n\n// Ваша функция isPrime здесь\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    \n    // Вызовите isPrime и выведите результат\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"7\", \"output\": \"1\"}, {\"input\": \"10\", \"output\": \"0\"}, {\"input\": \"17\", \"output\": \"1\"}]}",
            25,
            Task.Difficulty.MEDIUM,
            lesson3,
            skillFunctions,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000007"),
            "Обратный массив",
            "Напишите программу, которая переворачивает массив (первый элемент становится последним и наоборот).\n\nВходные данные:\nПервая строка: N (1 ≤ N ≤ 1000) - размер массива\nВторая строка: N целых чисел через пробел\n\nВыходные данные: элементы массива в обратном порядке через пробел\n\nПример:\nВход:\n5\n1 2 3 4 5\nВыход: 5 4 3 2 1",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    int arr[1000];\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\\n1 2 3 4 5\", \"output\": \"5 4 3 2 1\"}, {\"input\": \"3\\n10 20 30\", \"output\": \"30 20 10\"}]}",
            20,
            Task.Difficulty.MEDIUM,
            lesson2,
            skillArrays,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000008"),
            "Подсчет символов в строке",
            "Напишите программу, которая подсчитывает количество символов в строке (без использования strlen).\n\nВходные данные: строка (максимум 1000 символов)\nВыходные данные: количество символов в строке\n\nПример:\nВход: Hello\nВыход: 5",
            "#include <stdio.h>\n\nint main() {\n    char str[1001];\n    scanf(\"%s\", str);\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"Hello\", \"output\": \"5\"}, {\"input\": \"C\", \"output\": \"1\"}]}",
            15,
            Task.Difficulty.MEDIUM,
            lesson5,
            skillStrings,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000009"),
            "Обмен значений через указатели",
            "Напишите функцию swap(int *a, int *b), которая меняет местами значения двух переменных, используя указатели.\n\nВходные данные: два целых числа\nВыходные данные: те же числа, но в обратном порядке\n\nПример:\nВход: 5 10\nВыход: 10 5",
            "#include <stdio.h>\n\n// Ваша функция swap здесь\n\nint main() {\n    int a, b;\n    scanf(\"%d %d\", &a, &b);\n    \n    // Вызовите swap и выведите результат\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5 10\", \"output\": \"10 5\"}, {\"input\": \"-3 7\", \"output\": \"7 -3\"}]}",
            30,
            Task.Difficulty.HARD,
            lesson4,
            skillPointers,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000010"),
            "Поиск подстроки",
            "Напишите функцию, которая ищет подстроку в строке и возвращает индекс первого вхождения, или -1 если подстрока не найдена.\n\nВходные данные:\nПервая строка: основная строка\nВторая строка: подстрока для поиска\n\nВыходные данные: индекс первого вхождения или -1\n\nПример:\nВход:\nHello World\nWorld\nВыход: 6",
            "#include <stdio.h>\n\n// Ваша функция поиска здесь\n\nint main() {\n    char str[1001], substr[1001];\n    scanf(\"%s\", str);\n    scanf(\"%s\", substr);\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"Hello\\nWorld\", \"output\": \"-1\"}, {\"input\": \"programming\\nram\", \"output\": \"2\"}]}",
            35,
            Task.Difficulty.HARD,
            lesson5,
            skillStrings,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000011"),
            "Сортировка массива",
            "Отсортируйте массив целых чисел по возрастанию (используйте любой алгоритм сортировки).\n\nВходные данные:\nПервая строка: N (1 ≤ N ≤ 1000) - размер массива\nВторая строка: N целых чисел через пробел\n\nВыходные данные: отсортированный массив\n\nПример:\nВход:\n5\n3 1 4 2 5\nВыход: 1 2 3 4 5",
            "#include <stdio.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    int arr[1000];\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\\n3 1 4 2 5\", \"output\": \"1 2 3 4 5\"}, {\"input\": \"4\\n10 5 20 1\", \"output\": \"1 5 10 20\"}]}",
            30,
            Task.Difficulty.HARD,
            lesson2,
            skillArrays,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000012"),
            "Двоичный поиск",
            "Реализуйте алгоритм двоичного поиска в отсортированном массиве.\n\nВходные данные:\nПервая строка: N (1 ≤ N ≤ 1000) - размер отсортированного массива\nВторая строка: N целых чисел в порядке возрастания\nТретья строка: число для поиска\n\nВыходные данные: индекс найденного элемента или -1\n\nПример:\nВход:\n5\n1 3 5 7 9\n5\nВыход: 2",
            "#include <stdio.h>\n\n// Ваша функция binarySearch здесь\n\nint main() {\n    int n, target;\n    scanf(\"%d\", &n);\n    int arr[1000];\n    \n    // Ваш код здесь\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\\n1 3 5 7 9\\n5\", \"output\": \"2\"}, {\"input\": \"4\\n2 4 6 8\\n3\", \"output\": \"-1\"}]}",
            50,
            Task.Difficulty.EXPERT,
            lesson2,
            skillArrays,
            teacher
        );
        
        createTaskIfNotExists(
            UUID.fromString("20000000-0000-0000-0000-000000000013"),
            "Работа с динамической памятью",
            "Напишите программу, которая создает динамический массив, заполняет его числами от 1 до N, и выводит сумму элементов.\n\nВходные данные: одно целое число N (1 ≤ N ≤ 1000)\nВыходные данные: сумма элементов массива\n\nПример:\nВход: 5\nВыход: 15\n\nПримечание: не забудьте освободить память!",
            "#include <stdio.h>\n#include <stdlib.h>\n\nint main() {\n    int n;\n    scanf(\"%d\", &n);\n    \n    // Ваш код здесь (используйте malloc и free)\n    \n    return 0;\n}",
            "{\"tests\": [{\"input\": \"5\", \"output\": \"15\"}, {\"input\": \"10\", \"output\": \"55\"}]}",
            40,
            Task.Difficulty.EXPERT,
            lesson4,
            skillPointers,
            teacher
        );
        
        // Создаем соревнования по 5 задач в каждом
        createChallenges(teacher);
        
        log.info("Инициализация тестовых данных завершена успешно!");
        log.info("Создано: 8 навыков, 8 уроков, 13 задач, несколько соревнований");
    }
    
    private void createChallenges(User teacher) {
        log.info("Создание соревнований...");
        
        // Получаем все задачи для создания соревнований
        List<Task> allTasks = taskRepository.findAll();
        if (allTasks.size() < 15) {
            log.warn("Недостаточно задач для создания соревнований. Требуется минимум 15 задач.");
            return;
        }
        
        // Соревнование 1: Основы программирования (первые 5 задач)
        createChallengeIfNotExists(
            UUID.fromString("30000000-0000-0000-0000-000000000001"),
            "Основы программирования",
            "Соревнование для начинающих программистов. Решите 5 базовых задач по циклам и условиям.",
            Challenge.Type.THEMATIC,
            allTasks.subList(0, 5),
            java.time.LocalDateTime.now().minusDays(2),
            java.time.LocalDateTime.now().plusDays(5),
            100,
            teacher
        );
        
        // Соревнование 2: Работа с массивами (следующие 5 задач)
        if (allTasks.size() >= 10) {
            createChallengeIfNotExists(
                UUID.fromString("30000000-0000-0000-0000-000000000002"),
                "Мастер массивов",
                "Продвинутое соревнование по работе с массивами. Покажите свои навыки в обработке данных!",
                Challenge.Type.WEEKLY,
                allTasks.subList(5, 10),
                java.time.LocalDateTime.now().minusDays(1),
                java.time.LocalDateTime.now().plusDays(6),
                150,
                teacher
            );
        }
        
        // Соревнование 3: Сложные алгоритмы (последние 5 задач)
        if (allTasks.size() >= 15) {
            createChallengeIfNotExists(
                UUID.fromString("30000000-0000-0000-0000-000000000003"),
                "Алгоритмический вызов",
                "Сложное соревнование для опытных программистов. Решите задачи на указатели и динамическую память.",
                Challenge.Type.THEMATIC,
                allTasks.subList(10, Math.min(15, allTasks.size())),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now().plusDays(7),
                200,
                teacher
            );
        }
        
        // Соревнование 4: Задача дня (одна случайная задача)
        if (!allTasks.isEmpty()) {
            Task dailyTask = allTasks.get(0);
            createChallengeIfNotExists(
                UUID.fromString("30000000-0000-0000-0000-000000000004"),
                "Задача дня",
                "Ежедневная задача для поддержания навыков программирования.",
                Challenge.Type.DAILY,
                java.util.Arrays.asList(dailyTask),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now().plusDays(1),
                50,
                teacher
            );
        }
        
        log.info("Создано {} соревнований", challengeRepository.count());
    }
    
    private Challenge createChallengeIfNotExists(UUID id, String title, String description,
                                                 Challenge.Type type, List<Task> tasks,
                                                 java.time.LocalDateTime startDate,
                                                 java.time.LocalDateTime endDate,
                                                 int xpReward, User teacher) {
        return challengeRepository.findById(id)
                .orElseGet(() -> {
                    log.debug("Создание соревнования: {} с {} задачами", title, tasks.size());
                    Challenge challenge = new Challenge(title, description, type, tasks);
                    challenge.setId(id);
                    challenge.setStartDate(startDate);
                    challenge.setEndDate(endDate);
                    challenge.setXpReward(xpReward);
                    challenge.setActive(true);
                    challenge.setCreatedBy(teacher);
                    return challengeRepository.save(challenge);
                });
    }
    
    private Skill createSkillIfNotExists(UUID id, String name, String description) {
        return skillRepository.findById(id)
                .orElseGet(() -> {
                    log.debug("Создание навыка: {}", name);
                    Skill skill = new Skill();
                    skill.setId(id);
                    skill.setName(name);
                    skill.setDescription(description);
                    skill.setMaxLevel(10);
                    return skillRepository.save(skill);
                });
    }
    
    private Lesson createLessonIfNotExists(UUID id, String title, String description, 
                                           String content, String topic, int orderNumber, User teacher) {
        return lessonRepository.findById(id)
                .orElseGet(() -> {
                    log.debug("Создание урока: {}", title);
                    Lesson lesson = new Lesson(title, description, content, topic);
                    lesson.setId(id);
                    lesson.setOrderNumber(orderNumber);
                    lesson.setCreatedBy(teacher);
                    lesson.setPublished(true);
                    return lessonRepository.save(lesson);
                });
    }
    
    private Task createTaskIfNotExists(UUID id, String title, String description,
                                       String codeTemplate, String testCases, int xpReward,
                                       Task.Difficulty difficulty, Lesson lesson, Skill skill, User teacher) {
        return taskRepository.findById(id)
                .orElseGet(() -> {
                    log.debug("Создание задачи: {}", title);
                    Task task = new Task(title, description, codeTemplate);
                    task.setId(id);
                    task.setTestCases(testCases);
                    task.setXpReward(xpReward);
                    task.setDifficulty(difficulty);
                    task.setLesson(lesson);
                    task.setSkill(skill);
                    task.setCreatedBy(teacher);
                    task.setPublished(true);
                    return taskRepository.save(task);
                });
    }
}


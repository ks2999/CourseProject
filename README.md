# Курсовой проект
## Платформа для обучения программированию с элементами геймификации

---

## Описание проекта
Курсовой проект представляет собой веб-приложение для обучения программированию с элементами геймификации.  
Система позволяет пользователям регистрироваться, проходить теоретические материалы, решать программные задачи, получать опыт, уровни и достижения.

Основной результат проекта — **разработанное и работающее приложение**, а не изучение отдельных технологий.

---

## Цель и задачи проекта

**Цель проекта** — разработка микросервисного веб-приложения для обучения программированию с элементами геймификации.

Для достижения поставленной цели решаются следующие задачи:
- **Анализ аналогичных решений**: обзор существующих образовательных платформ и сервисов с геймификацией.
- **Обоснование стека технологий**: выбор языков программирования, фреймворков, СУБД и вспомогательных инструментов.
- **Разработка архитектурного решения**: выбор типа архитектуры приложения и разбиение на компоненты/сервисы.
- **Реализация серверной части**: разработка микросервисов, REST API, механизма аутентификации и авторизации, работы с БД, логики геймификации.
- **Реализация клиентской части**: разработка веб-интерфейса для работы пользователя с системой.
- **Интеграция и развертывание**: настройка взаимодействия компонентов, подготовка окружения для запуска приложения.
- **Подготовка документации**: описание архитектуры, структуры репозитория и инструкций по запуску.

Особенности реализации (JWT-аутентификация, Spring Security, работа с JPA/Hibernate, использование Docker и т.п.) относятся к **способам реализации** и не являются самостоятельными целями проекта.

---

## Архитектура системы

В качестве основы выбрана **микросервисная архитектура**: приложение состоит из нескольких самостоятельных сервисов, каждый из которых отвечает за свою область ответственности и взаимодействует с другими по REST API.

Структура репозитория и соответствие компонентам системы:

- `api-gateway/` — API Gateway, единая точка входа для всех внешних HTTP-запросов к системе.
- `auth-service/` — сервис аутентификации и авторизации пользователей (работа с JWT-токенами, вход/регистрация).
- `user-service/` — сервис управления пользователями, их профилями, прогрессом и связанной информацией.
- `gamification-service/` — сервис геймификации (начисление опыта, уровни, достижения, обработка событий).
- `web-client/` — веб-клиент (frontend), реализованный на HTML/CSS/JavaScript, использующий REST API бэкенд-сервисов.
- `docker-compose.yml` (при наличии) — конфигурация для совместного запуска всех сервисов и СУБД.

Каждый сервис имеет собственную структуру:
- `src/main/java/...` — исходный код микросервиса.
- `src/main/resources/` — конфигурационные файлы (профили Spring, миграции БД и т.п.).

Сервисы используют отдельные базы данных (PostgreSQL), что соответствует подходу "database per service" в микросервисной архитектуре.

---

## Используемые технологии

**Backend:**
- Java 17
- Spring Boot 3 (Web, Security, Data JPA)
- Hibernate

**Безопасность:**
- JWT (access/refresh токены)
- BCrypt для хеширования паролей

**База данных и миграции:**
- PostgreSQL
- Flyway

**Frontend:**
- HTML
- CSS
- JavaScript (Fetch API)

**Инфраструктура:**
- (Опционально) Docker, Docker Compose — как средство развёртывания и локального запуска, а не как цель проекта.

---

## Основная функциональность

- Регистрация и аутентификация пользователей.
- Хранение и редактирование профиля пользователя.
- Работа с теоретическими материалами (уроками).
- Решение программных задач с автоматической проверкой.
- Начисление опыта (XP), система уровней и достижений.
- Отображение прогресса и геймификационных метрик на стороне клиента.

---

## Структура репозитория (кратко)

- `api-gateway/` — реализация шлюза для маршрутизации запросов.
- `auth-service/` — микросервис аутентификации и авторизации.
- `user-service/` — микросервис управления пользователями и их данными.
- `gamification-service/` — микросервис логики геймификации.
- `web-client/` — статический веб-клиент (HTML/CSS/JS).
- `README.md` — общее описание проекта, архитектуры и структуры репозитория.

---

## Структура каталогов по компонентам

### API Gateway (`api-gateway/`)

```text
api-gateway/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/apigateway/
│       │       └── ApiGatewayApplication.java   - точка входа и конфигурация шлюза
│       └── resources/
│           └── application.yml                  - конфигурация маршрутизации и сервисов
├── build.gradle                                 - Gradle-конфигурация модуля
└── gradle*, gradlew*, settings.gradle           - служебные файлы сборки
```

### Сервис аутентификации (`auth-service/`)

```text
auth-service/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/authservice/
│       │       └── AuthServiceApplication.java  - точка входа сервиса аутентификации
│       └── resources/
│           └── application.yml                  - настройки профилей, БД и безопасности
├── build.gradle
└── gradle*, gradlew*, settings.gradle
```

### Сервис геймификации (`gamification-service/`)

```text
gamification-service/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/gamificationservice/
│       │       └── GamificationServiceApplication.java - точка входа сервиса геймификации
│       └── resources/
│           └── application.yml                         - базовая конфигурация сервиса
├── build.gradle
└── gradle*, gradlew*, settings.gradle
```

### Сервис управления пользователями и задачами (`user-service/`)

```text
user-service/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/
│       │       ├── Main.java                      - общий entry point (при необходимости)
│       │       └── users/
│       │           ├── UsersServiceApplication.java - точка входа микросервиса
│       │           ├── config/                    - конфигурация приложения
│       │           │   ├── CorsConfig.java        - CORS-настройки
│       │           │   ├── DataInitializer.java   - начальное наполнение БД (уроки, задания и т.п.)
│       │           │   ├── FlywayConfig.java      - конфигурация миграций Flyway
│       │           │   ├── LessonContent.java     - заготовки теоретических материалов
│       │           │   ├── OpenApiConfig.java     - конфигурация Swagger / OpenAPI
│       │           │   ├── PasswordEncoderConfig.java - настройки шифрования паролей
│       │           │   └── SecurityConfig.java    - конфигурация Spring Security
│       │           ├── controller/                - REST-контроллеры
│       │           │   ├── UserController.java    - управление пользователями и профилем
│       │           │   ├── LessonController.java  - работа с теоретическими материалами
│       │           │   ├── TaskController.java    - задачи для решения
│       │           │   ├── SubmissionController.java - отправка и проверка решений
│       │           │   ├── ChallengeController.java  - соревнования и челленджи
│       │           │   ├── StudentProgressController.java - прогресс по курсу
│       │           │   ├── StudentSkillController.java    - навыки/скиллы
│       │           │   ├── SkillController.java   - справочник навыков
│       │           │   ├── FileController.java    - загрузка и выдача файлов (аватарки)
│       │           │   └── GlobalExceptionHandler.java - обработка ошибок REST-слоя
│       │           ├── dto/                       - DTO для запросов и ответов
│       │           │   ├── UserRequest.java, UserUpdateRequest.java, UserResponse.java
│       │           │   ├── LessonRequest.java, LessonResponse.java
│       │           │   ├── TaskRequest.java, TaskResponse.java
│       │           │   ├── SubmissionRequest.java, SubmissionResponse.java
│       │           │   ├── ChallengeRequest.java, ChallengeResponse.java
│       │           │   ├── SkillRequest.java, SkillResponse.java
│       │           │   └── StudentProgressResponse.java, StudentSkillResponse.java
│       │           ├── model/                     - доменные сущности JPA
│       │           │   ├── User.java, Role.java, Permission.java
│       │           │   ├── Lesson.java, Task.java, Submission.java
│       │           │   ├── Challenge.java
│       │           │   ├── Skill.java, StudentSkill.java
│       │           │   ├── StudentProgress.java
│       │           │   ├── Achievement.java
│       │           │   └── Badge.java
│       │           ├── repository/                - Spring Data JPA репозитории
│       │           │   ├── UserRepository.java
│       │           │   ├── LessonRepository.java
│       │           │   ├── TaskRepository.java
│       │           │   ├── SubmissionRepository.java
│       │           │   ├── ChallengeRepository.java
│       │           │   ├── SkillRepository.java
│       │           │   ├── StudentSkillRepository.java
│       │           │   ├── StudentProgressRepository.java
│       │           │   ├── AchievementRepository.java
│       │           │   └── BadgeRepository.java
│       │           └── service/                   - бизнес-логика
│       │               ├── UserService.java       - управление пользователями и профилем
│       │               ├── LessonService.java     - работа с уроками
│       │               ├── TaskService.java       - CRUD по задачам
│       │               ├── SubmissionService.java - проверка решений, агрегация результатов
│       │               ├── CodeExecutionService.java - компиляция/запуск кода, прогоны тестов
│       │               ├── ChallengeService.java  - создание и проведение соревнований
│       │               ├── SkillService.java, StudentSkillService.java
│       │               ├── StudentProgressService.java
│       │               ├── AchievementService.java, BadgeService.java
│       │               ├── FileService.java       - работа с файлами (аватары)
│       │               └── PasswordService.java, RoleService.java
│       └── resources/
│           ├── application.yml                    - основная конфигурация сервиса
│           ├── application-dev.yml                - dev-профиль, настройки H2/PostgreSQL
│           └── db/migration/                      - Flyway-миграции
│               ├── V1__create_tables.sql          - создание основных таблиц
│               ├── V2__insert_sample_data.sql     - тестовые данные (уроки, задачи и т.п.)
│               └── V3__update_challenges.sql      - обновление/дополнение данных по челленджам
├── build.gradle
└── gradle*, gradlew*, settings.gradle
```

### Frontend (`web-client/`)

```text
web-client/
├── index.html          - главная страница (авторизация, список соревнований/задач)
├── profile.html        - профиль пользователя, редактирование данных и аватарки
├── progress.html       - страница прогресса (XP, уровни, выполненные задания)
├── achievements.html   - достижения и, при необходимости, лидерборд
├── lessons.html        - список и просмотр теоретических материалов (уроков)
├── tasks.html          - список задач и интерфейс решения задач
├── css/
│   ├── style.css       - общие стили приложения, сетка, оформление страниц
│   └── code-editor.css - оформление встроенного редактора кода и области вывода результатов
└── js/
    ├── api.js          - обёртки над REST API backend-сервисов
    ├── auth.js         - логика авторизации/регистрации, работа с токенами
    ├── profile.js      - загрузка/редактирование профиля, загрузка аватарки
    ├── progress.js     - загрузка и отображение прогресса пользователя
    ├── achievements.js - загрузка и отображение достижений
    ├── lessons.js      - работа с теоретическими материалами
    ├── tasks.js        - список задач, отправка решений, вывод результатов проверки
    ├── task-create.js  - интерфейс создания задач (роль преподавателя)
    ├── challenges.js   - отображение соревнований/челленджей
    ├── challenge-create.js - создание соревнований из набора задач (роль преподавателя)
    └── markdown.js     - парсинг и отображение markdown-контента уроков и условий задач
```

Более детальное текстовое описание архитектуры и структуры каталогов может быть оформлено в отдельном документе по типу "Текста программы" для ВКР.

---

## Запуск проекта

Конкретные шаги запуска зависят от конфигурации окружения (локальный запуск сервисов через IDE/Maven или запуск в Docker).  
При наличии `docker-compose.yml` запуск возможен командой:

```bash
docker-compose up --build
```

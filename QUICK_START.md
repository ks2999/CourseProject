# Быстрый старт

## Запуск User Service

### 1. Запустить PostgreSQL

```bash
cd /Users/kirillerkaev/Desktop/CourseProject
docker-compose up -d postgres-user
```

Проверить, что база данных запущена:
```bash
docker ps | grep postgres-user
```

### 2. Запустить приложение

```bash
cd user-service
./gradlew bootRun
```

Или через IDE: запустите `UsersServiceApplication.java`

### 3. Проверить работу

Приложение будет доступно на `http://localhost:8083`

Проверить здоровье:
```bash
curl http://localhost:8083/api/users
```

Создать тестового пользователя:
```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "name": "Тестовый пользователь"
  }'
```

## Структура созданных компонентов

✅ **Модели (12 моделей):**
- User, Role, Permission
- Lesson, Task, Submission
- Skill, StudentSkill, StudentProgress
- Achievement, Badge, Challenge

✅ **Репозитории (10 репозиториев):**
- UserRepository, LessonRepository, TaskRepository
- SubmissionRepository, SkillRepository, StudentSkillRepository
- StudentProgressRepository, AchievementRepository
- BadgeRepository, ChallengeRepository

✅ **Сервисы:**
- UserService, PasswordService, RoleService

✅ **Контроллеры:**
- UserController (REST API)
- GlobalExceptionHandler (обработка ошибок)

✅ **Конфигурация:**
- SecurityConfig (Spring Security)
- PasswordEncoderConfig

✅ **Миграции:**
- V1__create_tables.sql (создание всех таблиц)

## API Endpoints

- `POST /api/users` - Создать пользователя
- `GET /api/users/{id}` - Получить пользователя
- `GET /api/users` - Список всех пользователей
- `PUT /api/users/{id}` - Обновить пользователя (включая аватарку)
- `DELETE /api/users/{id}` - Удалить пользователя

## Особенности

- ✅ Все пользователи могут редактировать свой профиль (включая аватарку)
- ✅ Пароли хешируются с помощью BCrypt
- ✅ Email валидируется
- ✅ Flyway миграции создают все таблицы автоматически
- ✅ Spring Security настроен (пока разрешены все запросы)

## Следующие шаги

1. Добавить JWT аутентификацию
2. Реализовать контроллеры для уроков и задач
3. Добавить логику геймификации (XP, уровни, достижения)
4. Реализовать проверку кода и запуск тестов


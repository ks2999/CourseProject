# User Service

Микросервис для управления пользователями платформы обучения программированию.

## Запуск

### 1. Запустить PostgreSQL

Используйте Docker Compose из корня проекта:

```bash
cd /Users/kirillerkaev/Desktop/CourseProject
docker-compose up -d postgres-user
```

Или запустите PostgreSQL локально и создайте базу данных:

```sql
CREATE DATABASE user_db;
```

### 2. Настроить application.yml

Убедитесь, что в `src/main/resources/application.yml` указаны правильные параметры подключения к БД.

### 3. Запустить приложение

```bash
./gradlew bootRun
```

Или через IDE запустите `UsersServiceApplication`.

## API Endpoints

### Пользователи

- `POST /api/users` - Создать пользователя
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users` - Получить всех пользователей
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

## Примеры запросов

### Создать пользователя

```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "password123",
    "name": "Иван Иванов"
  }'
```

### Получить пользователя

```bash
curl http://localhost:8083/api/users/{id}
```

### Обновить пользователя (включая аватарку)

```bash
curl -X PUT http://localhost:8083/api/users/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Иван Петров",
    "avatar": "https://example.com/avatar.jpg"
  }'
```

## Структура проекта

- `model/` - Модели данных (User, Lesson, Task, Skill и т.д.)
- `repository/` - JPA репозитории
- `service/` - Бизнес-логика
- `controller/` - REST контроллеры
- `config/` - Конфигурация (Security, PasswordEncoder)
- `dto/` - Объекты передачи данных

## База данных

Миграции Flyway находятся в `src/main/resources/db/migration/`.

При первом запуске автоматически создадутся все таблицы.


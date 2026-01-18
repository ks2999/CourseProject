# Быстрый запуск

## Вариант 1: Без Docker (H2 база данных) - РЕКОМЕНДУЕТСЯ

Приложение уже настроено для работы без Docker!

```bash
cd user-service
./gradlew bootRun
```

После запуска откройте:
- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **API**: http://localhost:8083/api/users

## Вариант 2: С Docker (PostgreSQL)

Если хотите использовать PostgreSQL:

1. **Запустите Docker Desktop** (приложение на Mac)

2. Дождитесь, пока Docker полностью запустится (иконка в строке меню)

3. Запустите PostgreSQL:
```bash
cd /Users/kirillerkaev/Desktop/CourseProject
docker-compose up -d postgres-user
```

4. Проверьте, что контейнер запущен:
```bash
docker ps | grep postgres-user
```

5. Измените `application.yml` - уберите или закомментируйте строку:
```yaml
spring:
  profiles:
    active: dev  # <-- уберите или закомментируйте эту строку
```

6. Запустите приложение:
```bash
cd user-service
./gradlew bootRun
```

## Проверка работы

После запуска приложения (любым способом):

1. Откройте Swagger UI: http://localhost:8083/swagger-ui.html

2. Создайте тестового пользователя через Swagger:
   - Найдите `POST /api/users`
   - Нажмите "Try it out"
   - Вставьте JSON:
   ```json
   {
     "email": "test@example.com",
     "password": "password123",
     "name": "Тестовый пользователь"
   }
   ```
   - Нажмите "Execute"

3. Проверьте список пользователей:
   - Найдите `GET /api/users`
   - Нажмите "Try it out" → "Execute"

## Отличия H2 и PostgreSQL

**H2 (без Docker):**
- ✅ Быстрый запуск
- ✅ Не требует Docker
- ❌ Данные в памяти (исчезнут после остановки)
- ❌ Не подходит для продакшена

**PostgreSQL (с Docker):**
- ✅ Постоянное хранение данных
- ✅ Подходит для продакшена
- ❌ Требует запущенный Docker

## Устранение проблем

### Ошибка "Cannot connect to Docker daemon"
- Запустите Docker Desktop
- Дождитесь полного запуска (иконка перестанет мигать)
- Или используйте вариант без Docker (H2)

### Ошибка "role postgres does not exist"
- Используйте профиль `dev` (H2) - уже настроено по умолчанию
- Или убедитесь, что Docker контейнер запущен


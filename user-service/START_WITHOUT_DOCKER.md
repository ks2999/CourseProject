# Запуск без Docker (с H2 базой данных)

Если Docker не запущен, приложение автоматически использует H2 базу данных в памяти.

## Запуск

```bash
cd user-service
./gradlew bootRun
```

Приложение запустится с профилем `dev`, который использует H2 базу данных.

## Доступные URL

- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **API**: http://localhost:8083/api/users
- **H2 Console**: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:userdb`
  - Username: `sa`
  - Password: (пусто)

## Особенности H2 профиля

- ✅ База данных создается автоматически при запуске
- ✅ Все таблицы создаются через Hibernate (ddl-auto: create-drop)
- ✅ Flyway отключен
- ✅ Данные хранятся в памяти (исчезнут после остановки приложения)
- ✅ Не требует Docker или PostgreSQL

## Переключение на PostgreSQL

Когда Docker будет запущен, измените в `application.yml`:

```yaml
spring:
  profiles:
    active: # уберите 'dev' или закомментируйте эту строку
```

Или запустите с параметром:

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Запуск с PostgreSQL (когда Docker доступен)

1. Запустите Docker Desktop
2. Запустите PostgreSQL:
```bash
cd /Users/kirillerkaev/Desktop/CourseProject
docker-compose up -d postgres-user
```
3. Измените профиль в `application.yml` или запустите без профиля dev


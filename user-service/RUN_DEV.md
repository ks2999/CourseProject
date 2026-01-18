# Запуск в режиме разработки (H2)

## Проблема

Миграции Flyway написаны для PostgreSQL и не работают с H2. Для разработки нужно использовать H2 без Flyway.

## Решение

Запустите приложение с явным указанием профиля dev:

```bash
cd user-service
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Или через IDE:
- Добавьте VM options: `-Dspring.profiles.active=dev`
- Или Program arguments: `--spring.profiles.active=dev`

## Что делает профиль dev:

- ✅ Использует H2 базу данных в памяти
- ✅ Отключает Flyway (таблицы создаются через Hibernate)
- ✅ Включает H2 Console на http://localhost:8083/h2-console
- ✅ Автоматически создает все таблицы при запуске

## После запуска:

- Swagger UI: http://localhost:8083/swagger-ui.html
- H2 Console: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:userdb`
  - Username: `sa`
  - Password: (пусто)

## Для продакшена (PostgreSQL):

Уберите параметр профиля или используйте:
```bash
./gradlew bootRun
```

Тогда будут использоваться настройки PostgreSQL из `application.yml`.


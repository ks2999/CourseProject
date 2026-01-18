# Swagger UI - Документация API

## Доступ к Swagger UI

После запуска приложения Swagger UI будет доступен по адресу:

**http://localhost:8083/swagger-ui.html**

Или альтернативный путь:
**http://localhost:8083/swagger-ui/index.html**

## API Documentation

OpenAPI спецификация доступна по адресу:
**http://localhost:8083/v3/api-docs**

## Использование

1. Запустите приложение:
```bash
./gradlew bootRun
```

2. Откройте браузер и перейдите по адресу:
```
http://localhost:8083/swagger-ui.html
```

3. В Swagger UI вы сможете:
   - Просмотреть все доступные endpoints
   - Увидеть описание каждого метода
   - Протестировать API прямо в браузере
   - Увидеть примеры запросов и ответов

## Примеры использования в Swagger

### Создание пользователя

1. Найдите endpoint `POST /api/users`
2. Нажмите "Try it out"
3. Заполните JSON:
```json
{
  "email": "test@example.com",
  "password": "password123",
  "name": "Тестовый пользователь"
}
```
4. Нажмите "Execute"
5. Увидите ответ с созданным пользователем

### Получение пользователя

1. Найдите endpoint `GET /api/users/{id}`
2. Нажмите "Try it out"
3. Введите UUID пользователя (из предыдущего запроса)
4. Нажмите "Execute"

### Обновление пользователя (включая аватарку)

1. Найдите endpoint `PUT /api/users/{id}`
2. Нажмите "Try it out"
3. Введите UUID пользователя
4. Заполните JSON:
```json
{
  "name": "Обновленное имя",
  "avatar": "https://example.com/avatar.jpg"
}
```
5. Нажмите "Execute"

## Особенности

- Все endpoints документированы с описаниями
- Примеры запросов и ответов включены
- Валидация полей отображается в документации
- Можно тестировать API без использования Postman или curl


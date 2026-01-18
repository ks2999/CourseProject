package com.example.users.controller;

import com.example.users.dto.UserRequest;
import com.example.users.dto.UserResponse;
import com.example.users.dto.UserUpdateRequest;
import com.example.users.model.User;
import com.example.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API для управления пользователями")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Регистрация нового пользователя в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Данные нового пользователя", required = true)
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по его идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь найден",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей в системе")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя (имя, email, аватарка)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
                content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
        @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Данные для обновления", required = true)
            @Valid @RequestBody UserUpdateRequest request) {
        // TODO: Получить текущего пользователя из SecurityContext
        // Временно разрешаем обновление - в реальном приложении нужно получать из JWT токена
        User currentUser = new User();
        currentUser.setId(id);
        UserResponse response = userService.updateUser(id, request, currentUser);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы (только для администраторов)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
        @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID id) {
        // TODO: Получить текущего пользователя из SecurityContext
        // Временно разрешаем удаление - в реальном приложении нужна проверка прав
        User currentUser = new User();
        currentUser.setId(id);
        userService.deleteUser(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

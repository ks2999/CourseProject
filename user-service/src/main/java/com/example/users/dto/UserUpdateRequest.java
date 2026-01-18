package com.example.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление пользователя")
public class UserUpdateRequest {
    
    @Schema(description = "Email пользователя", example = "user@example.com")
    @Email(message = "Email should be valid")
    private String email;
    
    @Schema(description = "Имя пользователя", example = "Иван Петров")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @Schema(description = "URL аватарки", example = "https://example.com/avatar.jpg")
    private String avatar; // URL или путь к аватарке
    
    public UserUpdateRequest() {
    }
    
    public UserUpdateRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }
    
    public UserUpdateRequest(String email, String name, String avatar) {
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}


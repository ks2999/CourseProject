package com.example.users.model;

public enum Permission {
    USER_READ("Чтение данных пользователей"),
    USER_WRITE("Изменение данных пользователей"),
    USER_DELETE("Удаление пользователей"),
    PROFILE_EDIT("Изменение собственного профиля"),
    ADMIN_ACCESS("Доступ к административным функциям");
    
    private final String description;
    
    Permission(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}


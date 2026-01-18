package com.example.users.model;

import java.util.Set;

public enum Role {
    USER("Обычный пользователь", Set.of(
            Permission.PROFILE_EDIT
    )),
    ADMIN("Администратор системы", Set.of(
            Permission.USER_READ,
            Permission.USER_WRITE,
            Permission.USER_DELETE,
            Permission.PROFILE_EDIT,
            Permission.ADMIN_ACCESS
    )),
    STUDENT("Студент", Set.of(
            Permission.USER_READ,
            Permission.PROFILE_EDIT
    )),
    TEACHER("Преподаватель", Set.of(
            Permission.USER_READ,
            Permission.USER_WRITE,
            Permission.PROFILE_EDIT
    )),
    PROGRAMMER("Программист", Set.of(
            Permission.USER_READ,
            Permission.USER_WRITE,
            Permission.PROFILE_EDIT
    ));
    
    private final String description;
    private final Set<Permission> permissions;
    
    Role(String description, Set<Permission> permissions) {
        this.description = description;
        this.permissions = permissions;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    /**
     * Проверяет, имеет ли роль указанное разрешение
     * @param permission разрешение для проверки
     * @return true если роль имеет разрешение
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
    
    /**
     * Проверяет, является ли роль административной
     * @return true если роль ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Проверяет, может ли роль читать данные пользователей
     * @return true если роль может читать
     */
    public boolean canReadUsers() {
        return hasPermission(Permission.USER_READ) || isAdmin();
    }
    
    /**
     * Проверяет, может ли роль изменять данные пользователей
     * @return true если роль может изменять
     */
    public boolean canWriteUsers() {
        return hasPermission(Permission.USER_WRITE) || isAdmin();
    }
    
    /**
     * Проверяет, может ли роль изменять собственный профиль
     * Все роли могут изменять свой профиль
     * @return true (всегда)
     */
    public boolean canEditOwnProfile() {
        return true; // Все пользователи могут изменять свой профиль
    }
    
    /**
     * Проверяет, может ли роль удалять пользователей
     * @return true если роль может удалять
     */
    public boolean canDeleteUsers() {
        return hasPermission(Permission.USER_DELETE) || isAdmin();
    }
}

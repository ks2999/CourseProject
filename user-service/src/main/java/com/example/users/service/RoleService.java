package com.example.users.service;

import com.example.users.model.Permission;
import com.example.users.model.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    /**
     * Получить все доступные роли
     * @return список всех ролей
     */
    public List<Role> getAllRoles() {
        return Arrays.asList(Role.values());
    }
    
    /**
     * Получить описание роли
     * @param role роль
     * @return описание роли
     */
    public String getRoleDescription(Role role) {
        return role.getDescription();
    }
    
    /**
     * Получить все разрешения для роли
     * @param role роль
     * @return набор разрешений
     */
    public Set<Permission> getRolePermissions(Role role) {
        return role.getPermissions();
    }
    
    /**
     * Проверить, имеет ли роль разрешение
     * @param role роль
     * @param permission разрешение
     * @return true если роль имеет разрешение
     */
    public boolean hasPermission(Role role, Permission permission) {
        return role.hasPermission(permission);
    }
    
    /**
     * Получить список ролей с их описаниями
     * @return список строк в формате "ROLE - Описание"
     */
    public List<String> getRolesWithDescriptions() {
        return Arrays.stream(Role.values())
                .map(role -> role.name() + " - " + role.getDescription())
                .collect(Collectors.toList());
    }
    
    /**
     * Найти роль по имени (case-insensitive)
     * @param roleName имя роли
     * @return роль или null если не найдена
     */
    public Role findRoleByName(String roleName) {
        try {
            return Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Проверить, является ли роль административной
     * @param role роль
     * @return true если роль ADMIN
     */
    public boolean isAdminRole(Role role) {
        return role.isAdmin();
    }
}


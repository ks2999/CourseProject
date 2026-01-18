package com.example.users.dto;

import com.example.users.model.Permission;
import com.example.users.model.Role;

import java.util.Set;

/**
 * DTO для передачи информации о роли
 */
public class RoleInfo {
    
    private Role role;
    private String description;
    private Set<Permission> permissions;
    private boolean isAdmin;
    
    public RoleInfo() {
    }
    
    public RoleInfo(Role role) {
        this.role = role;
        this.description = role.getDescription();
        this.permissions = role.getPermissions();
        this.isAdmin = role.isAdmin();
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            this.description = role.getDescription();
            this.permissions = role.getPermissions();
            this.isAdmin = role.isAdmin();
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}


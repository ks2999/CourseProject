package com.example.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    
    private static final java.util.regex.Pattern EMAIL_PATTERN = 
        java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", 
             message = "Email format is invalid")
    private String email;
    
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @Column
    private String avatar; // URL или путь к аватарке
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    @Column(nullable = false)
    private Boolean enabled = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {
    }
    
    public User(String email, String password, String name) {
        validateEmail(email);
        this.email = email;
        this.password = password; // Password should be hashed before setting
        this.name = name;
    }
    
    // Factory method for creating a new user (password will be hashed externally)
    public static User create(String email, String hashedPassword, String name) {
        User user = new User(email, hashedPassword, name);
        return user;
    }
    
    // Factory method with role
    public static User create(String email, String hashedPassword, String name, Role role) {
        User user = create(email, hashedPassword, name);
        user.setRole(role);
        return user;
    }
    
    // Factory method for admin
    public static User createAdmin(String email, String hashedPassword, String name) {
        return create(email, hashedPassword, name, Role.ADMIN);
    }
    
    // Email validation
    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    
    // Method to verify email format
    public boolean isValidEmail() {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    // Method to get role description
    public String getRoleDescription() {
        return role.getDescription();
    }
    
    // Method to check if user has specific permission
    public boolean hasPermission(Permission permission) {
        return role.hasPermission(permission);
    }
    
    // Method to check if user is admin
    public boolean isAdmin() {
        return role.isAdmin();
    }
    
    // Method to check if user can read other users
    public boolean canReadUsers() {
        return role.canReadUsers();
    }
    
    // Method to check if user can modify other users
    public boolean canWriteUsers() {
        return role.canWriteUsers();
    }
    
    // Method to check if user can delete other users
    public boolean canDeleteUsers() {
        return role.canDeleteUsers();
    }
    
    // Method to check if user can edit own profile (always true)
    public boolean canEditOwnProfile() {
        return true; // Все пользователи могут редактировать свой профиль
    }
    
    // Method to check if user can edit another user's profile
    public boolean canEditUserProfile(User otherUser) {
        // Пользователь может редактировать свой профиль или если у него есть права USER_WRITE
        return this.id.equals(otherUser.getId()) || canWriteUsers();
    }
    
    // Method to get all user permissions
    public Set<Permission> getPermissions() {
        return role.getPermissions();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        // Password should already be hashed when setting
        // Use service layer to hash passwords before setting
        this.password = password;
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
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
              
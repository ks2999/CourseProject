package com.example.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    @Value("${app.upload.avatar-dir:avatars}")
    private String avatarDir;
    
    /**
     * Сохраняет загруженный файл аватарки
     * @param file загруженный файл
     * @param userId ID пользователя
     * @return URL для доступа к файлу
     */
    public String saveAvatar(MultipartFile file, UUID userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }
        
        // Проверяем тип файла
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Файл должен быть изображением");
        }
        
        // Проверяем размер файла (максимум 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Размер файла не должен превышать 5MB");
        }
        
        // Создаем директорию для аватарок, если её нет
        Path avatarPath = Paths.get(uploadDir, avatarDir);
        if (!Files.exists(avatarPath)) {
            Files.createDirectories(avatarPath);
            log.info("Создана директория для аватарок: {}", avatarPath);
        }
        
        // Генерируем уникальное имя файла
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = userId.toString() + "_" + UUID.randomUUID().toString() + extension;
        
        // Сохраняем файл
        Path targetPath = avatarPath.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        log.info("Аватарка сохранена: {}", targetPath);
        
        // Возвращаем URL для доступа к файлу
        return "/api/files/avatars/" + filename;
    }
    
    /**
     * Удаляет файл аватарки
     */
    public void deleteAvatar(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return;
        }
        
        try {
            // Извлекаем имя файла из URL
            String filename = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir, avatarDir, filename);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Аватарка удалена: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Ошибка при удалении аватарки: {}", avatarUrl, e);
        }
    }
}


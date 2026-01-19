package com.example.users.controller;

import com.example.users.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "API для работы с файлами")
public class FileController {
    
    private final FileService fileService;
    
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @PostMapping("/avatars")
    @Operation(summary = "Загрузить аватарку", description = "Загружает изображение аватарки для пользователя")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId) {
        try {
            String avatarUrl = fileService.saveAvatar(file, userId);
            Map<String, String> response = new HashMap<>();
            response.put("avatarUrl", avatarUrl);
            response.put("message", "Аватарка успешно загружена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/avatars/{filename:.+}")
    @Operation(summary = "Получить аватарку", description = "Возвращает файл аватарки по имени")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/avatars").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                // Определяем тип файла по расширению
                MediaType mediaType = MediaType.IMAGE_JPEG;
                String lowerFilename = filename.toLowerCase();
                if (lowerFilename.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (lowerFilename.endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                } else if (lowerFilename.endsWith(".webp")) {
                    mediaType = MediaType.valueOf("image/webp");
                }
                
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}


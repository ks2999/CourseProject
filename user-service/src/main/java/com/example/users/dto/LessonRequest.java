package com.example.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание/обновление урока")
public class LessonRequest {
    
    @Schema(description = "Название урока", example = "Введение в циклы")
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @Schema(description = "Краткое описание урока", example = "Изучаем основы циклов в C")
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    
    @Schema(description = "Теоретический материал урока")
    private String content;
    
    @Schema(description = "Тема урока", example = "Циклы")
    private String topic;
    
    @Schema(description = "Порядковый номер урока", example = "1")
    private Integer orderNumber;
    
    @Schema(description = "Опубликован ли урок", example = "true")
    private Boolean published = false;
    
    public LessonRequest() {
    }
    
    public LessonRequest(String title, String description, String content, String topic) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.topic = topic;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public Integer getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public Boolean getPublished() {
        return published;
    }
    
    public void setPublished(Boolean published) {
        this.published = published;
    }
}


package com.example.users.repository;

import com.example.users.model.Lesson;
import com.example.users.model.Task;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByPublishedTrue();
    List<Task> findByLesson(Lesson lesson);
    List<Task> findByCreatedBy(User user);
    List<Task> findByLessonAndPublishedTrue(Lesson lesson);
}


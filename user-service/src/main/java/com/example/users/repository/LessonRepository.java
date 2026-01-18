package com.example.users.repository;

import com.example.users.model.Lesson;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findByPublishedTrueOrderByOrderNumberAsc();
    List<Lesson> findByCreatedBy(User user);
    List<Lesson> findByTopic(String topic);
}


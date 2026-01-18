package com.example.users.repository;

import com.example.users.model.Submission;
import com.example.users.model.Task;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findByUser(User user);
    List<Submission> findByTask(Task task);
    List<Submission> findByUserAndTask(User user, Task task);
    Optional<Submission> findFirstByUserAndTaskOrderByCreatedAtDesc(User user, Task task);
    long countByUserAndStatus(User user, Submission.Status status);
}


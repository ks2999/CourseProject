package com.example.users.repository;

import com.example.users.model.StudentProgress;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, UUID> {
    Optional<StudentProgress> findByUser(User user);
    
    @Query("SELECT sp FROM StudentProgress sp ORDER BY sp.totalXp DESC")
    List<StudentProgress> findAllOrderByTotalXpDesc();
    
    @Query("SELECT sp FROM StudentProgress sp ORDER BY sp.level DESC, sp.totalXp DESC")
    List<StudentProgress> findAllOrderByLevelDesc();
}


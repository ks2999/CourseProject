package com.example.users.repository;

import com.example.users.model.Skill;
import com.example.users.model.StudentSkill;
import com.example.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, UUID> {
    List<StudentSkill> findByUser(User user);
    Optional<StudentSkill> findByUserAndSkill(User user, Skill skill);
}


package com.example.dadi.repository;

import com.example.dadi.model.Quiz;
import com.example.dadi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreatedBy(User user);
    List<Quiz> findByIsActive(boolean isActive);
    List<Quiz> findByCreatedByAndIsActive(User user, boolean isActive);
    boolean existsByTitleAndCreatedBy(String title, User createdBy);
}

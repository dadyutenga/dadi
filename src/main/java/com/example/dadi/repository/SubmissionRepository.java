package com.example.dadi.repository;

import com.example.dadi.model.Quiz;
import com.example.dadi.model.Submission;
import com.example.dadi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUser(User user);
    List<Submission> findByQuiz(Quiz quiz);
    List<Submission> findByUserAndQuiz(User user, Quiz quiz);
    int countByUserAndQuiz(User user, Quiz quiz);
    boolean existsByUserAndQuiz(User user, Quiz quiz);
}

package com.example.dadi.repository;

import com.example.dadi.model.Question;
import com.example.dadi.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
    int countByQuiz(Quiz quiz);
    void deleteByQuiz(Quiz quiz);
}

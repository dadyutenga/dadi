package com.example.dadi.service;

import com.example.dadi.model.Quiz;
import com.example.dadi.model.User;
import com.example.dadi.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz createQuiz(Quiz quiz, User createdBy) {
        if (quizRepository.existsByTitleAndCreatedBy(quiz.getTitle(), createdBy)) {
            throw new RuntimeException("You already have a quiz with this title!");
        }
        quiz.setCreatedBy(createdBy);
        quiz.setCreatedAt(LocalDateTime.now());
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<Quiz> findQuizzesByUser(User user) {
        return quizRepository.findByCreatedBy(user);
    }

    public List<Quiz> findActiveQuizzes() {
        return quizRepository.findByIsActive(true);
    }

    public List<Quiz> findUserActiveQuizzes(User user) {
        return quizRepository.findByCreatedByAndIsActive(user, true);
    }

    public Page<Quiz> findAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public void activateQuiz(Long quizId) {
        quizRepository.findById(quizId).ifPresent(quiz -> {
            quiz.setActive(true);
            quizRepository.save(quiz);
        });
    }

    public void deactivateQuiz(Long quizId) {
        quizRepository.findById(quizId).ifPresent(quiz -> {
            quiz.setActive(false);
            quizRepository.save(quiz);
        });
    }
}

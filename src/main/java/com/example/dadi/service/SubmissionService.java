package com.example.dadi.service;

import com.example.dadi.model.Quiz;
import com.example.dadi.model.Submission;
import com.example.dadi.model.User;
import com.example.dadi.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserService userService;
    private final QuizService quizService;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, 
                           UserService userService, 
                           QuizService quizService) {
        this.submissionRepository = submissionRepository;
        this.userService = userService;
        this.quizService = quizService;
    }

    public Submission createSubmission(Long userId, Long quizId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        // Check if user already has a submission for this quiz
        if (submissionRepository.existsByUserAndQuiz(user, quiz)) {
            throw new RuntimeException("You have already submitted this quiz!");
        }

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setQuiz(quiz);
        submission.setSubmittedAt(LocalDateTime.now());
        
        return submissionRepository.save(submission);
    }

    public Submission updateSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Optional<Submission> findById(Long id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> findByUserId(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return submissionRepository.findByUser(user);
    }

    public List<Submission> findByQuizId(Long quizId) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        return submissionRepository.findByQuiz(quiz);
    }

    public Optional<Submission> findByUserAndQuiz(Long userId, Long quizId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        return submissionRepository.findByUserAndQuiz(user, quiz);
    }

    public int countSubmissionsByUserAndQuiz(Long userId, Long quizId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        return submissionRepository.countByUserAndQuiz(user, quiz);
    }

    public void deleteSubmission(Long submissionId) {
        submissionRepository.deleteById(submissionId);
    }
}

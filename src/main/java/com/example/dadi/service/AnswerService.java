package com.example.dadi.service;

import com.example.dadi.model.Answer;
import com.example.dadi.model.Question;
import com.example.dadi.model.Submission;
import com.example.dadi.repository.AnswerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final SubmissionService submissionService;
    private final QuestionService questionService;

    @Autowired
    public AnswerService(AnswerRepository answerRepository,
                        SubmissionService submissionService,
                        QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.submissionService = submissionService;
        this.questionService = questionService;
    }

    public Answer submitAnswer(Answer answer, Long submissionId, Long questionId) {
        Submission submission = submissionService.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        // Check if answer already exists for this submission and question
        List<Answer> existingAnswers = answerRepository.findBySubmissionAndQuestion(submission, question);
        
        if (!existingAnswers.isEmpty()) {
            // Update existing answer (assuming one answer per submission and question)
            Answer current = existingAnswers.get(0);
            if (answer.getSelectedOption() != null) {
                current.setSelectedOption(answer.getSelectedOption());
                current.setIsCorrect(answer.getSelectedOption().isCorrect());
            }
            if (answer.getAnswerText() != null) {
                current.setAnswerText(answer.getAnswerText());
            }
            if (answer.getPointsAwarded() != null) {
                current.setPointsAwarded(answer.getPointsAwarded());
            }
            return answerRepository.save(current);
        } else {
            // Create new answer
            answer.setSubmission(submission);
            answer.setQuestion(question);
            if (answer.getSelectedOption() != null) {
                answer.setIsCorrect(answer.getSelectedOption().isCorrect());
            }
            return answerRepository.save(answer);
        }
    }

    public List<Answer> findBySubmissionId(Long submissionId) {
        Submission submission = submissionService.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));
        return answerRepository.findBySubmission(submission);
    }

    public List<Answer> findByQuestionId(Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        return answerRepository.findByQuestion(question);
    }

    public Optional<Answer> findBySubmissionAndQuestion(Long submissionId, Long questionId) {
        Submission submission = submissionService.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        List<Answer> answers = answerRepository.findBySubmissionAndQuestion(submission, question);
        return answers.isEmpty() ? Optional.empty() : Optional.of(answers.get(0));
    }

    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }

    public void deleteAllBySubmission(Long submissionId) {
        Submission submission = submissionService.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));
        answerRepository.deleteBySubmission(submission);
    }
}

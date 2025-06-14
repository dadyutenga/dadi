package com.example.dadi.repository;

import com.example.dadi.model.Answer;
import com.example.dadi.model.Question;
import com.example.dadi.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findBySubmission(Submission submission);
    List<Answer> findByQuestion(Question question);
    List<Answer> findBySubmissionAndQuestion(Submission submission, Question question);
    boolean existsBySubmissionAndQuestion(Submission submission, Question question);
    void deleteBySubmission(Submission submission);
}

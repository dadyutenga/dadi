package com.example.dadi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "answer_text", length = 2000)
    private String answerText;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "points_awarded")
    private Integer pointsAwarded = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
    
    // Constructors
    public Answer() {
    }
    
    public Answer(Question question, Submission submission, String answerText) {
        this.question = question;
        this.submission = submission;
        this.answerText = answerText;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    public Boolean getIsCorrect() {
        return isCorrect;
    }
    public void setIsCorrect(Boolean correct) {
        isCorrect = correct;
    }
    public Integer getPointsAwarded() {
        return pointsAwarded;
    }
    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public Submission getSubmission() {
        return submission;
    }
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }
    public Option getSelectedOption() {
        return selectedOption;
    }
    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }
}

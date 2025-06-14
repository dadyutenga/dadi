package com.example.dadi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard_entries")
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(name = "total_score", nullable = false)
    private int totalScore = 0;
    
    @Column(name = "total_time_seconds", nullable = false)
    private long totalTimeSeconds = 0;
    
    @Column(name = "completed_quizzes", nullable = false)
    private int completedQuizzes = 0;
    
    @Column(name = "rank_position")
    private Integer rankPosition;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    
    // Constructors
    public LeaderboardEntry() {
    }
    
    public LeaderboardEntry(User user, Quiz quiz, int totalScore, long totalTimeSeconds) {
        this.user = user;
        this.quiz = quiz;
        this.username = user.getUsername();
        this.totalScore = totalScore;
        this.totalTimeSeconds = totalTimeSeconds;
        this.completedQuizzes = 1;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    public long getTotalTimeSeconds() {
        return totalTimeSeconds;
    }
    public void setTotalTimeSeconds(long totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }
    public int getCompletedQuizzes() {
        return completedQuizzes;
    }
    public void setCompletedQuizzes(int completedQuizzes) {
        this.completedQuizzes = completedQuizzes;
    }
    public Integer getRankPosition() {
        return rankPosition;
    }
    public void setRankPosition(Integer rankPosition) {
        this.rankPosition = rankPosition;
    }
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

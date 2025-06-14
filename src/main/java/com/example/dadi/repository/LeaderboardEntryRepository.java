package com.example.dadi.repository;

import com.example.dadi.model.LeaderboardEntry;
import com.example.dadi.model.Quiz;
import com.example.dadi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {
    List<LeaderboardEntry> findByQuizOrderByTotalScoreDesc(Quiz quiz);
    
    @Query("SELECT l FROM LeaderboardEntry l WHERE l.quiz = :quiz ORDER BY l.totalScore DESC, l.totalTimeSeconds ASC")
    List<LeaderboardEntry> findLeaderboardByQuiz(@Param("quiz") Quiz quiz, Pageable pageable);
    
    Optional<LeaderboardEntry> findByUserAndQuiz(User user, Quiz quiz);
    
    @Query("SELECT l FROM LeaderboardEntry l WHERE l.quiz = :quiz ORDER BY l.totalScore DESC, l.totalTimeSeconds ASC")
    List<LeaderboardEntry> findTopNByQuizOrderByTotalScoreDesc(@Param("quiz") Quiz quiz, Pageable pageable);
    
    int countByQuiz(Quiz quiz);
}

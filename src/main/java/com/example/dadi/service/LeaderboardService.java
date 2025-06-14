package com.example.dadi.service;

import com.example.dadi.model.LeaderboardEntry;
import com.example.dadi.model.Quiz;
import com.example.dadi.model.Submission;
import com.example.dadi.model.User;
import com.example.dadi.repository.LeaderboardEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeaderboardService {
    private final LeaderboardEntryRepository leaderboardRepository;
    private final UserService userService;
    private final QuizService quizService;
    private final SubmissionService submissionService;

    @Autowired
    public LeaderboardService(LeaderboardEntryRepository leaderboardRepository,
                            UserService userService,
                            QuizService quizService,
                            SubmissionService submissionService) {
        this.leaderboardRepository = leaderboardRepository;
        this.userService = userService;
        this.quizService = quizService;
        this.submissionService = submissionService;
    }

    public LeaderboardEntry updateLeaderboard(Long submissionId) {
        Submission submission = submissionService.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        // Calculate total score and time from answers
        int totalScore = submission.getAnswers() != null ? 
                submission.getAnswers().stream()
                        .mapToInt(answer -> answer.getPointsAwarded() != null ? answer.getPointsAwarded() : 0)
                        .sum() : 0;
        
        // Use the submission's submittedAt time to calculate time taken
        int totalTimeSeconds = submission.getTimeTakenSeconds() != null ? 
                submission.getTimeTakenSeconds().intValue() : 0;

        // Find existing leaderboard entry or create new one
        LeaderboardEntry entry = leaderboardRepository.findByUserAndQuiz(submission.getUser(), submission.getQuiz())
                .orElse(new LeaderboardEntry());

        // Update entry
        entry.setUser(submission.getUser());
        entry.setQuiz(submission.getQuiz());
        entry.setTotalScore(totalScore);
        entry.setTotalTimeSeconds(totalTimeSeconds);
        entry.setLastUpdated(LocalDateTime.now());

        // Save the entry
        LeaderboardEntry savedEntry = leaderboardRepository.save(entry);
        
        // Update ranks for the quiz
        updateRanksForQuiz(submission.getQuiz().getId());
        
        return savedEntry;
    }

    private void updateRanksForQuiz(Long quizId) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        // Get all entries for the quiz, ordered by score (desc) and time (asc)
        List<LeaderboardEntry> entries = leaderboardRepository.findByQuizOrderByTotalScoreDesc(quiz);
        
        // Sort by score (desc) and time (asc)
        entries.sort(Comparator.comparing(LeaderboardEntry::getTotalScore).reversed()
                .thenComparing(LeaderboardEntry::getTotalTimeSeconds));
        
        // Update ranks
        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            entry.setRankPosition(rank++);
            leaderboardRepository.save(entry);
        }
    }

    public List<LeaderboardEntry> getLeaderboardForQuiz(Long quizId) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        // Get all entries and sort them
        List<LeaderboardEntry> entries = leaderboardRepository.findByQuizOrderByTotalScoreDesc(quiz);
        entries.sort(Comparator.comparing(LeaderboardEntry::getTotalScore).reversed()
                .thenComparing(LeaderboardEntry::getTotalTimeSeconds));
        
        // Update ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRankPosition(i + 1);
        }
        
        return entries;
    }

    public Optional<LeaderboardEntry> getUserRanking(Long userId, Long quizId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        return leaderboardRepository.findByUserAndQuiz(user, quiz);
    }

    public Page<LeaderboardEntry> getTopNByQuiz(Long quizId, int count) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        List<LeaderboardEntry> entries = leaderboardRepository.findByQuizOrderByTotalScoreDesc(quiz);
        
        // Sort by score (desc) and time (asc)
        entries.sort(Comparator.comparing(LeaderboardEntry::getTotalScore).reversed()
                .thenComparing(LeaderboardEntry::getTotalTimeSeconds));
        
        // Update ranks and limit to top N
        List<LeaderboardEntry> topN = entries.stream()
                .limit(count)
                .collect(Collectors.toList());
                
        for (int i = 0; i < topN.size(); i++) {
            topN.get(i).setRankPosition(i + 1);
        }
        
        return new PageImpl<>(topN, PageRequest.of(0, count), topN.size());
    }

    public int getTotalParticipants(Long quizId) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        
        return leaderboardRepository.countByQuiz(quiz);
    }
}

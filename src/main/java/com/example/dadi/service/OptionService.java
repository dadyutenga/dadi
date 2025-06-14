package com.example.dadi.service;

import com.example.dadi.model.Option;
import com.example.dadi.model.Question;
import com.example.dadi.repository.OptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OptionService {
    private final OptionRepository optionRepository;
    private final QuestionService questionService;

    @Autowired
    public OptionService(OptionRepository optionRepository, QuestionService questionService) {
        this.optionRepository = optionRepository;
        this.questionService = questionService;
    }


    public Option createOption(Option option, Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        option.setQuestion(question);
        return optionRepository.save(option);
    }

    public Option updateOption(Option option) {
        return optionRepository.save(option);
    }

    public void deleteOption(Long optionId) {
        optionRepository.deleteById(optionId);
    }

    public List<Option> findByQuestionId(Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        return optionRepository.findByQuestion(question);
    }

    public int countOptionsByQuestion(Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        return optionRepository.countByQuestion(question);
    }

    public void deleteAllByQuestion(Long questionId) {
        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        optionRepository.deleteByQuestion(question);
    }
}

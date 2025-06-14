package com.example.dadi.repository;

import com.example.dadi.model.Option;
import com.example.dadi.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestion(Question question);
    int countByQuestion(Question question);
    void deleteByQuestion(Question question);
}

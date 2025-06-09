package com.quiz.aws.backend.repository;

import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.model.WrongAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WrongAnswerRepository extends JpaRepository<WrongAnswer, Long> {
    List<WrongAnswer> findByUser(User user);
    boolean existsByUserAndQuestionId(User user, Integer questionId); // 추가

    WrongAnswer findByUserAndQuestionId(User user, Integer questionId);
}
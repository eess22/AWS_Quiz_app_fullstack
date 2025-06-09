package com.quiz.aws.backend.service;

import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.model.WrongAnswer;
import com.quiz.aws.backend.repository.WrongAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WrongAnswerService {

    private final WrongAnswerRepository wrongAnswerRepository;

    public void saveWrongAnswer(User user, Integer questionId) {
        boolean exists = wrongAnswerRepository.existsByUserAndQuestionId(user, questionId);
        if (!exists) {
            WrongAnswer wrongAnswer = WrongAnswer.builder()
                    .user(user)
                    .questionId(questionId)
                    .timestamp(LocalDateTime.now())
                    .build();
            wrongAnswerRepository.save(wrongAnswer);
        }
    }
}
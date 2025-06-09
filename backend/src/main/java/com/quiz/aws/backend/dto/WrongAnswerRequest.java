package com.quiz.aws.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrongAnswerRequest {
    private Integer questionId;
}
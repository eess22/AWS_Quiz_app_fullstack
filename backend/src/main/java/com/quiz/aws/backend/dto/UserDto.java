package com.quiz.aws.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String role;
    private boolean isBanned;
    private LocalDateTime createdAt;
}
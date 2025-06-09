package com.quiz.aws.backend.service;

import com.quiz.aws.backend.dto.UserDto;
import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(String search) {
        List<User> users = (search == null || search.isBlank())
                ? userRepository.findAll()
                : userRepository.findByEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(search, search);

        return users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getUsername())
                        .role(user.getRole())
                        .isBanned(user.isBanned())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void toggleRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getRole().equals("ADMIN")) {
            user.setRole("USER");
        } else {
            user.setRole("ADMIN");
        }
        userRepository.save(user);
    }

    public void toggleBan(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBanned(!user.isBanned());
        userRepository.save(user);
    }
}
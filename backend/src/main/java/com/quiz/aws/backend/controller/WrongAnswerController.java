package com.quiz.aws.backend.controller;

import com.quiz.aws.backend.dto.WrongAnswerRequest;
import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.model.WrongAnswer;
import com.quiz.aws.backend.repository.WrongAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wrong")
@RequiredArgsConstructor
public class WrongAnswerController {

    private final WrongAnswerRepository wrongAnswerRepository;

    @PostMapping
    public ResponseEntity<?> saveWrongAnswer(@RequestBody WrongAnswerRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // ✅ 중복 여부 확인
        boolean alreadyExists = wrongAnswerRepository.existsByUserAndQuestionId(user, request.getQuestionId());
        if (alreadyExists) {
            return ResponseEntity.ok("이미 저장된 오답입니다.");
        }

        // ✅ 저장
        WrongAnswer wrong = WrongAnswer.builder()
                .questionId(request.getQuestionId())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();

        wrongAnswerRepository.save(wrong);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<Integer>> getMyWrongAnswers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // 사용자 기준으로 오답 조회 후 questionId만 추출
        List<Integer> questionIds = wrongAnswerRepository.findByUser(user).stream()
                .map(WrongAnswer::getQuestionId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(questionIds);
    }
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteWrongAnswer(@PathVariable Integer questionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        WrongAnswer wrong = wrongAnswerRepository.findByUserAndQuestionId(user, questionId);
        if (wrong != null) {
            wrongAnswerRepository.delete(wrong);
            return ResponseEntity.ok("오답 삭제 성공");
        } else {
            return ResponseEntity.notFound().build(); // 이미 없거나 잘못된 요청
        }
    }
}
package com.quiz.aws.backend.controller;

import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final Path uploadDir = Paths.get("uploads");

    public UserController() {
        try {
            Files.createDirectories(uploadDir); // uploads 폴더 없으면 생성
        } catch (Exception e) {
            log.error("파일 저장 폴더 생성 실패", e);
        }
    }

    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 여기서 User로 직접 캐스팅
        User user = (User) authentication.getPrincipal();
        String email = user.getEmail();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        try {
            String fileName = email + ".png";
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // DB에 프로필 이미지 경로 저장
            user.setProfileImage("/api/user/profile-image/view/" + email);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("url", user.getProfileImage());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
        }
    }
    // [3] 프로필 이미지 불러오기
    @GetMapping("/profile-image/view/{email}")
    public ResponseEntity<Resource> viewImage(@PathVariable String email) {
        try {
            Path filePath = uploadDir.resolve(email + ".png");
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource image = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
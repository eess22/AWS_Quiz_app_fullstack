package com.quiz.aws.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quiz.aws.backend.model.User;
import com.quiz.aws.backend.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println(" JWT 토큰 발견: " + token);
                    break;
                }
            }
        }

        if (token != null) {
            String email = jwtUtil.getEmailFromToken(token);
            System.out.println(" JWT에서 추출한 이메일: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(email).orElse(null);
                System.out.println(" DB에서 사용자 찾기 결과: " + (user != null));

                if (user != null && jwtUtil.validateToken(token)) {
                    // ✅ banned 사용자면 요청 막기
                    if (user.isBanned()) {
                        System.out.println("정지된 계정입니다.");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("정지된 계정입니다.");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println(" JWT 유효성 검사 성공");
                } else {
                    System.out.println(" 유저 없음 또는 JWT 유효성 실패");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
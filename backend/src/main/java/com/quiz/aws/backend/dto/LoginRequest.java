package com.quiz.aws.backend.dto;

public class LoginRequest {
    private String email;
    private String password;
    private String username;

    // 기본 생성자
    public LoginRequest() {
    }

    // getter, setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
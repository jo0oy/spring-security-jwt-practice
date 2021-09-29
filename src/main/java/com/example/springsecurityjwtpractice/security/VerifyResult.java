package com.example.springsecurityjwtpractice.security;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerifyResult {
    private boolean success;
    private String username;

    @Builder
    public VerifyResult(boolean success, String username) {
        this.success = success;
        this.username = username;
    }
}

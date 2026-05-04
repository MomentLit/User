package com.example.user.global.security;

import lombok.Getter;

@Getter
public class UserPrincipal {

    private final String userId;

    public UserPrincipal(String userId) {
        this.userId = userId;
    }
}

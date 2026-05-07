package com.example.user.dto.response;

import com.example.user.entity.Role;
import com.example.user.entity.User;

public record UserAuthResponse(
        String userId,
        String name,
        String role
) {
    public static UserAuthResponse from(User user) {
        return new UserAuthResponse(
                user.getId(),
                user.getName(),
                user.getRole().name()
        );
    }
}

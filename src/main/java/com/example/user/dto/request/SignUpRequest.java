package com.example.user.dto;

import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotNull
        String email,

        @NotNull
        String password,

        @NotNull
        String name
) {
}

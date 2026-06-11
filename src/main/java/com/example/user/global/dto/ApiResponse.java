package com.example.user.global.dto;

public record ApiResponse<T>(
        String message,
        T data
) {
}
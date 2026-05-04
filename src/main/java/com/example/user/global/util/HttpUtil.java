package com.example.user.global.util;

import com.example.user.global.dto.ApiResponse;

public class HttpUtil {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null);
    }
}

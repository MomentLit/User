package com.example.user.controller;

import com.example.user.global.security.JwtProvider;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @GetMapping("/token")
    public String createToken(@RequestParam String userId) {
        userService.getMyProfile(userId); // 유저 없으면 여기서 예외 발생
        return jwtProvider.createTestToken(userId);
    }
}
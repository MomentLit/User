package com.example.user.controller;

import com.example.user.dto.request.SignInRequest;
import com.example.user.dto.request.UserGoogleOauthRequest;
import com.example.user.dto.response.UserAuthResponse;
import com.example.user.service.InternalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class InternalUserController {

    private final InternalUserService internalUserService;

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthResponse> authenticate(
            @RequestBody SignInRequest request
    ) {
        UserAuthResponse response = internalUserService.authenticate(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<UserAuthResponse> authenticateGoogle(
            @RequestBody UserGoogleOauthRequest request
            ) {
        return ResponseEntity.ok(internalUserService.authenticateGoogle(request));
    }
}

package com.example.user.service;

import com.example.user.dto.request.SignInRequest;
import com.example.user.dto.request.UserGoogleOauthRequest;
import com.example.user.dto.response.UserAuthResponse;
import com.example.user.dto.response.UserNameResponse;
import com.example.user.entity.User;
import com.example.user.global.exception.BadRequestException;
import com.example.user.global.exception.DeletedUserException;
import com.example.user.global.exception.DuplicateEmailException;
import com.example.user.global.exception.InvalidPasswordException;
import com.example.user.global.exception.UserNotFoundException;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserAuthResponse authenticate(SignInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        validateActiveUser(user);
        validatePassword(request.password(), user.getPassword());

        return UserAuthResponse.from(user);
    }

    @Transactional
    public UserAuthResponse authenticateGoogle(UserGoogleOauthRequest request) {
        if (!Boolean.TRUE.equals(request.emailVerified())) {
            throw new BadRequestException("Google 이메일 인증이 필요합니다.");
        }

        User user = findOrCreateGoogleUser(request);

        return UserAuthResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserNameResponse getUserName(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        validateActiveUser(user);

        return UserNameResponse.from(user);
    }

    // 삭제 여부 확인
    private void validateActiveUser(User user) {
        if (user.getDeletedAt() != null) {
            throw new DeletedUserException("삭제된 유저");
        }
    }

    // 비밀번호 확인
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    private User findOrCreateGoogleUser(UserGoogleOauthRequest request) {
        return userRepository
                .findByAuthProviderAndProviderIdAndDeletedAtIsNull("GOOGLE", request.providerId())
                .orElseGet(() -> createGoogleUser(request));
    }

    private User createGoogleUser(UserGoogleOauthRequest request) {
        userRepository.findByEmailAndDeletedAtIsNull(request.email())
                .ifPresent(existingUser -> {
                    throw new DuplicateEmailException("이미 가입된 이메일입니다.");
                });

        User user = User.createGoogle(
                request.email(),
                request.name(),
                request.imageUrl(),
                request.providerId()
        );

        return userRepository.save(user);
    }
}

package com.example.user.service;

import com.example.user.dto.request.SignInRequest;
import com.example.user.dto.response.UserAuthResponse;
import com.example.user.entity.User;
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
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            validateActiveUser(user);
            validatePassword(request.password(), user.getPassword());

            return UserAuthResponse.from(user);
        }

        // 삭제 여부 확인
        private void validateActiveUser(User user) {
            if (user.getDeletedAt() != null) {
                throw new IllegalStateException("삭제된 유저");
            }
        }

        // 비밀번호 확인
        private void validatePassword(String rawPassword, String encodedPassword) {
            if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }
}

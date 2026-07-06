package com.example.user.controller;

import com.example.user.dto.request.SignUpRequest;
import com.example.user.dto.request.UserUpdateRequest;
import com.example.user.dto.response.SignUpResponse;
import com.example.user.dto.response.UserSearchResponse;
import com.example.user.entity.Role;
import com.example.user.global.security.UserPrincipal;
import com.example.user.service.UserService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String USER_ID = "user-1";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new UserController(userService))
                .setCustomArgumentResolvers(new TestUserPrincipalArgumentResolver())
                .build();
    }

    @Test
    void signup() throws Exception {
        when(userService.signup(any(SignUpRequest.class)))
                .thenReturn(new SignUpResponse(USER_ID));

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@example.com",
                                  "password": "password123",
                                  "name": "Test User",
                                  "phone": "010-1234-5678"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("create user"))
                .andExpect(jsonPath("$.data.user_id").value(USER_ID));

        ArgumentCaptor<SignUpRequest> captor = ArgumentCaptor.forClass(SignUpRequest.class);
        verify(userService).signup(captor.capture());
        assertThat(captor.getValue().email()).isEqualTo("test@example.com");
        assertThat(captor.getValue().password()).isEqualTo("password123");
        assertThat(captor.getValue().name()).isEqualTo("Test User");
        assertThat(captor.getValue().phone()).isEqualTo("010-1234-5678");
    }

    @Test
    void getMyProfile() throws Exception {
        when(userService.getMyProfile(USER_ID))
                .thenReturn(new UserSearchResponse(
                        "https://example.com/profile.png",
                        "test@example.com",
                        "Test User",
                        "010-1234-5678",
                        "Hello, MomentLit",
                        LocalDateTime.of(2026, 5, 28, 12, 0)
                ));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("select my profile"))
                .andExpect(jsonPath("$.data.image_url").value("https://example.com/profile.png"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.intro").value("Hello, MomentLit"))
                .andExpect(jsonPath("$.data.created_at").exists());

        verify(userService).getMyProfile(USER_ID);
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated User",
                                  "image_url": "https://example.com/updated.png",
                                  "phone": "010-1234-5678",
                                  "intro": "Updated intro"
                                }
                                """))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UserUpdateRequest> captor = ArgumentCaptor.forClass(UserUpdateRequest.class);
        verify(userService).update(org.mockito.Mockito.eq(USER_ID), captor.capture());
        assertThat(captor.getValue().name()).isEqualTo("Updated User");
        assertThat(captor.getValue().imageUrl()).isEqualTo("https://example.com/updated.png");
        assertThat(captor.getValue().phone()).isEqualTo("010-1234-5678");
        assertThat(captor.getValue().intro()).isEqualTo("Updated intro");
    }

    @Test
    void deleteMyProfile() throws Exception {
        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isNoContent());

        verify(userService).delete(USER_ID);
    }

    private static final class TestUserPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                    && UserPrincipal.class.equals(parameter.getParameterType());
        }

        @Override
        public Object resolveArgument(
                MethodParameter parameter,
                ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                WebDataBinderFactory binderFactory
        ) {
            return new UserPrincipal(USER_ID, Role.USER);
        }
    }
}

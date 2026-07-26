package com.example.user.controller;

import com.example.user.dto.request.SignInRequest;
import com.example.user.dto.request.UserOauthRequest;
import com.example.user.dto.response.UserAuthResponse;
import com.example.user.service.InternalUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class InternalUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InternalUserService internalUserService;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new InternalUserController(internalUserService)).build();
    }

    @Test
    void authenticate() throws Exception {
        when(internalUserService.authenticate(any(SignInRequest.class)))
                .thenReturn(new UserAuthResponse("user-1", "Test User", "USER"));

        mockMvc.perform(post("/internal/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@example.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("user-1"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("USER"));

        ArgumentCaptor<SignInRequest> captor = ArgumentCaptor.forClass(SignInRequest.class);
        verify(internalUserService).authenticate(captor.capture());
        assertThat(captor.getValue().email()).isEqualTo("test@example.com");
        assertThat(captor.getValue().password()).isEqualTo("password123");
    }

    @Test
    void authenticateOauth() throws Exception {
        when(internalUserService.authenticateOauth(any(UserOauthRequest.class)))
                .thenReturn(new UserAuthResponse("google-user-1", "Google User", "USER"));

        mockMvc.perform(post("/internal/users/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "provider": "GOOGLE",
                                  "provider_id": "google-provider-id",
                                  "email": "google@example.com",
                                  "email_verified": true,
                                  "name": "Google User",
                                  "image_url": "https://example.com/google.png"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("google-user-1"))
                .andExpect(jsonPath("$.name").value("Google User"))
                .andExpect(jsonPath("$.role").value("USER"));

        ArgumentCaptor<UserOauthRequest> captor = ArgumentCaptor.forClass(UserOauthRequest.class);
        verify(internalUserService).authenticateOauth(captor.capture());
        assertThat(captor.getValue().provider()).isEqualTo("GOOGLE");
        assertThat(captor.getValue().providerId()).isEqualTo("google-provider-id");
        assertThat(captor.getValue().email()).isEqualTo("google@example.com");
        assertThat(captor.getValue().emailVerified()).isTrue();
        assertThat(captor.getValue().name()).isEqualTo("Google User");
        assertThat(captor.getValue().imageUrl()).isEqualTo("https://example.com/google.png");
    }
}

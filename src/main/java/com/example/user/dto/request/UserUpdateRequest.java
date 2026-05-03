package com.example.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserUpdateRequest(
        String name,

        @JsonProperty("image_url")
        String imageUrl
) {
}

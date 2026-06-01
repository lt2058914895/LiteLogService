package com.example.litelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileResponse {

    private Boolean success;
    private String message;
    private String nickname;
    private String avatarUrl;
}
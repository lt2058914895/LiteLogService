package com.example.litelog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 50, message = "昵称长度必须在1-50个字符之间")
    private String nickname;

    private String avatarUrl;
}
package com.example.litelog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendRequest {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "手机号格式不正确")
    private String phone;

    private String type = "login";
}
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
public class FeedbackSubmitRequest {

    @NotBlank(message = "反馈类型不能为空")
    @Size(max = 50, message = "反馈类型长度不能超过 50 个字符")
    private String type;

    @NotBlank(message = "反馈内容不能为空")
    @Size(max = 500, message = "反馈内容长度不能超过 500 个字符")
    private String message;

    @Size(max = 100, message = "邮箱长度不能超过 100 个字符")
    private String email;

    @Size(max = 50, message = "应用版本长度不能超过 50 个字符")
    private String appVersion;

    @Size(max = 100, message = "设备信息长度不能超过 100 个字符")
    private String deviceInfo;
}

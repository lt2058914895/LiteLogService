package com.example.litelog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public FeedbackSubmitRequest() {
    }

    public FeedbackSubmitRequest(String type, String message, String email, String appVersion, String deviceInfo) {
        this.type = type;
        this.message = message;
        this.email = email;
        this.appVersion = appVersion;
        this.deviceInfo = deviceInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}

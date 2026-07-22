package com.example.litelog.dto.response;

/**
 * 头像上传响应 DTO，替代 Map<String, Object>
 */
public class UploadAvatarResponse {

    private Boolean success;
    private String message;
    private String avatarUrl;

    public UploadAvatarResponse() {
    }

    public UploadAvatarResponse(Boolean success, String message, String avatarUrl) {
        this.success = success;
        this.message = message;
        this.avatarUrl = avatarUrl;
    }

    public static UploadAvatarResponseBuilder builder() {
        return new UploadAvatarResponseBuilder();
    }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public static class UploadAvatarResponseBuilder {
        private Boolean success;
        private String message;
        private String avatarUrl;

        public UploadAvatarResponseBuilder success(Boolean success) { this.success = success; return this; }
        public UploadAvatarResponseBuilder message(String message) { this.message = message; return this; }
        public UploadAvatarResponseBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }

        public UploadAvatarResponse build() {
            return new UploadAvatarResponse(success, message, avatarUrl);
        }
    }
}
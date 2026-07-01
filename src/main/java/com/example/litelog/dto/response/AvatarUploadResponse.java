package com.example.litelog.dto.response;

public class AvatarUploadResponse {

    private boolean success;
    private String message;
    private String avatarUrl;

    public AvatarUploadResponse() {
    }

    public AvatarUploadResponse(boolean success, String message, String avatarUrl) {
        this.success = success;
        this.message = message;
        this.avatarUrl = avatarUrl;
    }

    public static AvatarUploadResponseBuilder builder() {
        return new AvatarUploadResponseBuilder();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public static class AvatarUploadResponseBuilder {
        private boolean success;
        private String message;
        private String avatarUrl;

        public AvatarUploadResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AvatarUploadResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AvatarUploadResponseBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public AvatarUploadResponse build() {
            return new AvatarUploadResponse(success, message, avatarUrl);
        }
    }
}

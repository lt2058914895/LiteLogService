package com.example.litelog.dto.response;

/**
 * 获取用户ID响应 DTO，替代 Map<String, Object>
 */
public class GetUserIdResponse {

    private Boolean success;
    private String message;
    private Long userId;

    public GetUserIdResponse() {
    }

    public GetUserIdResponse(Boolean success, String message, Long userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public static GetUserIdResponseBuilder builder() {
        return new GetUserIdResponseBuilder();
    }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public static class GetUserIdResponseBuilder {
        private Boolean success;
        private String message;
        private Long userId;

        public GetUserIdResponseBuilder success(Boolean success) { this.success = success; return this; }
        public GetUserIdResponseBuilder message(String message) { this.message = message; return this; }
        public GetUserIdResponseBuilder userId(Long userId) { this.userId = userId; return this; }

        public GetUserIdResponse build() {
            return new GetUserIdResponse(success, message, userId);
        }
    }
}
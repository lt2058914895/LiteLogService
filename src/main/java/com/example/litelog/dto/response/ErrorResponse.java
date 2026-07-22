package com.example.litelog.dto.response;

/**
 * 统一错误响应 DTO，替代 GlobalExceptionHandler 中的 Map<String, Object>
 */
public class ErrorResponse {

    private Boolean success;
    private Integer code;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ErrorResponseBuilder {
        private Boolean success;
        private Integer code;
        private String message;

        public ErrorResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public ErrorResponseBuilder code(Integer code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(success, code, message);
        }
    }
}
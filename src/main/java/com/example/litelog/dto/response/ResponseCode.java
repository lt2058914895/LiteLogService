package com.example.litelog.dto.response;

public enum ResponseCode {
    
    SUCCESS(20000, "success"),
    FAILURE(50000, "failure"),
    SERVER_FAILURE(50001, "server_failure"),
    PARAM_ERROR(40000, "param_error"),
    UNAUTHORIZED(40100, "unauthorized"),
    FEEDBACK_TYPE_INVALID(40001, "feedback_type_invalid"),
    FEEDBACK_MESSAGE_EMPTY(40002, "feedback_message_empty"),
    FEEDBACK_MESSAGE_TOO_LONG(40003, "feedback_message_too_long");
    
    private final int code;
    private final String message;
    
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
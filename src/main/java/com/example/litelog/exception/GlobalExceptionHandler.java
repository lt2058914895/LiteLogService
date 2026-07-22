package com.example.litelog.exception;

import com.example.litelog.dto.response.ErrorResponse;
import com.example.litelog.dto.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("业务异常：code={}, message={}", e.getCode(), e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        HttpStatus status = HttpStatus.OK;
        if (e.getCode() >= 40000 && e.getCode() < 50000) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e.getCode() >= 50000) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("参数校验失败：{}", e.getMessage());

        StringBuilder message = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code(ResponseCode.PARAM_ERROR.getCode())
                .message(message.toString())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件上传大小超限：{}", e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code(ResponseCode.FAILURE.getCode())
                .message("上传文件大小超过限制")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("系统异常：", e);

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code(ResponseCode.SERVER_FAILURE.getCode())
                .message(ResponseCode.SERVER_FAILURE.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
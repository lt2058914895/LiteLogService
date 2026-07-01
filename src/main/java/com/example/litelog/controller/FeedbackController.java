package com.example.litelog.controller;

import com.example.litelog.dto.request.FeedbackSubmitRequest;
import com.example.litelog.dto.response.FeedbackSubmitResponse;
import com.example.litelog.service.FeedbackService;
import com.example.litelog.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @PostMapping("/submit")
    public ResponseEntity<FeedbackSubmitResponse> submitFeedback(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Id-Type", required = false, defaultValue = "device") String idType,
            @Valid @RequestBody FeedbackSubmitRequest request) {
        
        log.info("用户提交反馈：userId={}, idType={}, type={}", userId, idType, request.getType());
        
        Long userIdValue = null;
        if (userId != null && !userId.isEmpty()) {
            userIdValue = userService.getOrCreateUserId(userId, idType);
        }
        
        FeedbackSubmitResponse response = feedbackService.submitFeedback(request, userIdValue);
        return ResponseEntity.ok(response);
    }
}

package com.example.litelog.controller;

import com.example.litelog.dto.request.FeedbackSubmitRequest;
import com.example.litelog.dto.response.FeedbackSubmitResponse;
import com.example.litelog.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<FeedbackSubmitResponse> submitFeedback(
            @Valid @RequestBody FeedbackSubmitRequest request) {
        
        log.info("用户提交反馈：type={}", request.getType());
        
        FeedbackSubmitResponse response = feedbackService.submitFeedback(request, null);
        return ResponseEntity.ok(response);
    }
}

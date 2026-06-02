package com.example.litelog.service;

import com.example.litelog.dto.request.FeedbackSubmitRequest;
import com.example.litelog.dto.response.FeedbackSubmitResponse;

public interface FeedbackService {
    FeedbackSubmitResponse submitFeedback(FeedbackSubmitRequest request, Long userId);
}

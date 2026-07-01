package com.example.litelog.service.impl;

import com.example.litelog.dto.request.FeedbackSubmitRequest;
import com.example.litelog.dto.response.FeedbackSubmitResponse;
import com.example.litelog.dto.response.ResponseCode;
import com.example.litelog.entity.Feedback;
import com.example.litelog.repository.FeedbackRepository;
import com.example.litelog.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    @Transactional
    public FeedbackSubmitResponse submitFeedback(FeedbackSubmitRequest request, Long userId) {
        try {
            Feedback feedback = Feedback.builder()
                    .type(request.getType())
                    .message(request.getMessage())
                    .email(request.getEmail())
                    .appVersion(request.getAppVersion())
                    .deviceInfo(request.getDeviceInfo())
                    .userId(userId)
                    .build();

            Feedback savedFeedback = feedbackRepository.save(feedback);

            log.info("用户 {} 提交反馈成功，反馈 ID: {}", userId, savedFeedback.getId());

            return FeedbackSubmitResponse.builder()
                    .success(true)
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .feedbackId(savedFeedback.getId())
                    .build();

        } catch (Exception e) {
            log.error("提交反馈失败：{}", e.getMessage());
            return FeedbackSubmitResponse.builder()
                    .success(false)
                    .code(ResponseCode.FAILURE.getCode())
                    .message(ResponseCode.FAILURE.getMessage())
                    .feedbackId(null)
                    .build();
        }
    }
}

package com.example.litelog.service.impl;

import com.example.litelog.dto.request.FeedbackSubmitRequest;
import com.example.litelog.dto.response.FeedbackSubmitResponse;
import com.example.litelog.entity.Feedback;
import com.example.litelog.repository.FeedbackRepository;
import com.example.litelog.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

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
                    .message("反馈提交成功")
                    .feedbackId(savedFeedback.getId())
                    .build();

        } catch (Exception e) {
            log.error("提交反馈失败：{}", e.getMessage());
            return FeedbackSubmitResponse.builder()
                    .success(false)
                    .message("反馈提交失败：" + e.getMessage())
                    .feedbackId(null)
                    .build();
        }
    }
}

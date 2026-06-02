package com.example.litelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSubmitResponse {

    private Boolean success;
    private Integer code;
    private String message;
    private Long feedbackId;
}

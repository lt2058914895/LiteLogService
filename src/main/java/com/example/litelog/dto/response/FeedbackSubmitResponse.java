package com.example.litelog.dto.response;

public class FeedbackSubmitResponse {

    private Boolean success;
    private Integer code;
    private String message;
    private Long feedbackId;

    public FeedbackSubmitResponse() {
    }

    public FeedbackSubmitResponse(Boolean success, Integer code, String message, Long feedbackId) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.feedbackId = feedbackId;
    }

    public static FeedbackSubmitResponseBuilder builder() {
        return new FeedbackSubmitResponseBuilder();
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

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public static class FeedbackSubmitResponseBuilder {
        private Boolean success;
        private Integer code;
        private String message;
        private Long feedbackId;

        public FeedbackSubmitResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public FeedbackSubmitResponseBuilder code(Integer code) {
            this.code = code;
            return this;
        }

        public FeedbackSubmitResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public FeedbackSubmitResponseBuilder feedbackId(Long feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public FeedbackSubmitResponse build() {
            return new FeedbackSubmitResponse(success, code, message, feedbackId);
        }
    }
}

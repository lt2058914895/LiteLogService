package com.example.litelog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String appVersion;

    @Column(length = 100)
    private String deviceInfo;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Feedback() {
    }

    public Feedback(Long id, String type, String message, String email, String appVersion, String deviceInfo,
                    Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.email = email;
        this.appVersion = appVersion;
        this.deviceInfo = deviceInfo;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static FeedbackBuilder builder() {
        return new FeedbackBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class FeedbackBuilder {
        private Long id;
        private String type;
        private String message;
        private String email;
        private String appVersion;
        private String deviceInfo;
        private Long userId;
        private LocalDateTime createdAt;

        public FeedbackBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FeedbackBuilder type(String type) {
            this.type = type;
            return this;
        }

        public FeedbackBuilder message(String message) {
            this.message = message;
            return this;
        }

        public FeedbackBuilder email(String email) {
            this.email = email;
            return this;
        }

        public FeedbackBuilder appVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public FeedbackBuilder deviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
            return this;
        }

        public FeedbackBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public FeedbackBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Feedback build() {
            return new Feedback(id, type, message, email, appVersion, deviceInfo, userId, createdAt);
        }
    }
}

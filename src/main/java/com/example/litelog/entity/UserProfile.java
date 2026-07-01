package com.example.litelog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private Double height = 170.0;

    @Column(nullable = false)
    private Integer gender = 0;

    @Column(nullable = false)
    private Integer age = 30;

    @Column(name = "goal_weight", nullable = false)
    private Double goalWeight = 65.0;

    @Column(name = "goal_body_fat")
    private Double goalBodyFat;

    @Column(name = "goal_waist_circumference")
    private Double goalWaistCircumference;

    @Column(name = "goal_hip_circumference")
    private Double goalHipCircumference;

    @Column(name = "goal_chest_circumference")
    private Double goalChestCircumference;

    @Column(name = "goal_thigh_circumference")
    private Double goalThighCircumference;

    @Column(name = "weight_unit", nullable = false, length = 10)
    private String weightUnit = "kg";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserProfile() {
    }

    public UserProfile(Long id, User user, Double height, Integer gender, Integer age, Double goalWeight,
                       Double goalBodyFat, Double goalWaistCircumference, Double goalHipCircumference,
                       Double goalChestCircumference, Double goalThighCircumference, String weightUnit,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.height = height;
        this.gender = gender;
        this.age = age;
        this.goalWeight = goalWeight;
        this.goalBodyFat = goalBodyFat;
        this.goalWaistCircumference = goalWaistCircumference;
        this.goalHipCircumference = goalHipCircumference;
        this.goalChestCircumference = goalChestCircumference;
        this.goalThighCircumference = goalThighCircumference;
        this.weightUnit = weightUnit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(Double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public Double getGoalBodyFat() {
        return goalBodyFat;
    }

    public void setGoalBodyFat(Double goalBodyFat) {
        this.goalBodyFat = goalBodyFat;
    }

    public Double getGoalWaistCircumference() {
        return goalWaistCircumference;
    }

    public void setGoalWaistCircumference(Double goalWaistCircumference) {
        this.goalWaistCircumference = goalWaistCircumference;
    }

    public Double getGoalHipCircumference() {
        return goalHipCircumference;
    }

    public void setGoalHipCircumference(Double goalHipCircumference) {
        this.goalHipCircumference = goalHipCircumference;
    }

    public Double getGoalChestCircumference() {
        return goalChestCircumference;
    }

    public void setGoalChestCircumference(Double goalChestCircumference) {
        this.goalChestCircumference = goalChestCircumference;
    }

    public Double getGoalThighCircumference() {
        return goalThighCircumference;
    }

    public void setGoalThighCircumference(Double goalThighCircumference) {
        this.goalThighCircumference = goalThighCircumference;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static UserProfileBuilder builder() {
        return new UserProfileBuilder();
    }

    public static class UserProfileBuilder {
        private Long id;
        private User user;
        private Double height;
        private Integer gender;
        private Integer age;
        private Double goalWeight;
        private Double goalBodyFat;
        private Double goalWaistCircumference;
        private Double goalHipCircumference;
        private Double goalChestCircumference;
        private Double goalThighCircumference;
        private String weightUnit;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserProfileBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserProfileBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserProfileBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public UserProfileBuilder gender(Integer gender) {
            this.gender = gender;
            return this;
        }

        public UserProfileBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public UserProfileBuilder goalWeight(Double goalWeight) {
            this.goalWeight = goalWeight;
            return this;
        }

        public UserProfileBuilder goalBodyFat(Double goalBodyFat) {
            this.goalBodyFat = goalBodyFat;
            return this;
        }

        public UserProfileBuilder goalWaistCircumference(Double goalWaistCircumference) {
            this.goalWaistCircumference = goalWaistCircumference;
            return this;
        }

        public UserProfileBuilder goalHipCircumference(Double goalHipCircumference) {
            this.goalHipCircumference = goalHipCircumference;
            return this;
        }

        public UserProfileBuilder goalChestCircumference(Double goalChestCircumference) {
            this.goalChestCircumference = goalChestCircumference;
            return this;
        }

        public UserProfileBuilder goalThighCircumference(Double goalThighCircumference) {
            this.goalThighCircumference = goalThighCircumference;
            return this;
        }

        public UserProfileBuilder weightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
            return this;
        }

        public UserProfileBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserProfileBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(id, user, height, gender, age, goalWeight,
                    goalBodyFat, goalWaistCircumference, goalHipCircumference,
                    goalChestCircumference, goalThighCircumference, weightUnit,
                    createdAt, updatedAt);
        }
    }
}

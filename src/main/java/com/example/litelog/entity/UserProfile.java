package com.example.litelog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private Double height = 170.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer gender = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer age = 30;

    @Column(name = "goal_weight", nullable = false)
    @Builder.Default
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
    @Builder.Default
    private String weightUnit = "kg";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
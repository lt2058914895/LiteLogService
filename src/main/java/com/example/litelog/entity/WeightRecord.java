package com.example.litelog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record_id", nullable = false, unique = true)
    private String recordId;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "waist_circumference")
    private Double waistCircumference;

    @Column(name = "hip_circumference")
    private Double hipCircumference;

    @Column(name = "thigh_circumference")
    private Double thighCircumference;

    @Column(name = "measurement_time_period", length = 50)
    private String measurementTimePeriod;

    @Column(length = 500)
    private String note;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
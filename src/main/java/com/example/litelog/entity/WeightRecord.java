package com.example.litelog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_records")
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

    @Column(name = "chest_circumference")
    private Double chestCircumference;

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

    public WeightRecord() {
    }

    public WeightRecord(Long id, Long userId, String recordId, Double weight, Double bodyFatPercentage,
                        Double waistCircumference, Double hipCircumference, Double chestCircumference,
                        Double thighCircumference, String measurementTimePeriod, String note, String imageUrl,
                        LocalDateTime date, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.recordId = recordId;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.waistCircumference = waistCircumference;
        this.hipCircumference = hipCircumference;
        this.chestCircumference = chestCircumference;
        this.thighCircumference = thighCircumference;
        this.measurementTimePeriod = measurementTimePeriod;
        this.note = note;
        this.imageUrl = imageUrl;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WeightRecordBuilder builder() {
        return new WeightRecordBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Double getWaistCircumference() {
        return waistCircumference;
    }

    public void setWaistCircumference(Double waistCircumference) {
        this.waistCircumference = waistCircumference;
    }

    public Double getHipCircumference() {
        return hipCircumference;
    }

    public void setHipCircumference(Double hipCircumference) {
        this.hipCircumference = hipCircumference;
    }

    public Double getChestCircumference() {
        return chestCircumference;
    }

    public void setChestCircumference(Double chestCircumference) {
        this.chestCircumference = chestCircumference;
    }

    public Double getThighCircumference() {
        return thighCircumference;
    }

    public void setThighCircumference(Double thighCircumference) {
        this.thighCircumference = thighCircumference;
    }

    public String getMeasurementTimePeriod() {
        return measurementTimePeriod;
    }

    public void setMeasurementTimePeriod(String measurementTimePeriod) {
        this.measurementTimePeriod = measurementTimePeriod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public static class WeightRecordBuilder {
        private Long id;
        private Long userId;
        private String recordId;
        private Double weight;
        private Double bodyFatPercentage;
        private Double waistCircumference;
        private Double hipCircumference;
        private Double chestCircumference;
        private Double thighCircumference;
        private String measurementTimePeriod;
        private String note;
        private String imageUrl;
        private LocalDateTime date;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public WeightRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WeightRecordBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public WeightRecordBuilder recordId(String recordId) {
            this.recordId = recordId;
            return this;
        }

        public WeightRecordBuilder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public WeightRecordBuilder bodyFatPercentage(Double bodyFatPercentage) {
            this.bodyFatPercentage = bodyFatPercentage;
            return this;
        }

        public WeightRecordBuilder waistCircumference(Double waistCircumference) {
            this.waistCircumference = waistCircumference;
            return this;
        }

        public WeightRecordBuilder hipCircumference(Double hipCircumference) {
            this.hipCircumference = hipCircumference;
            return this;
        }

        public WeightRecordBuilder chestCircumference(Double chestCircumference) {
            this.chestCircumference = chestCircumference;
            return this;
        }

        public WeightRecordBuilder thighCircumference(Double thighCircumference) {
            this.thighCircumference = thighCircumference;
            return this;
        }

        public WeightRecordBuilder measurementTimePeriod(String measurementTimePeriod) {
            this.measurementTimePeriod = measurementTimePeriod;
            return this;
        }

        public WeightRecordBuilder note(String note) {
            this.note = note;
            return this;
        }

        public WeightRecordBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public WeightRecordBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public WeightRecordBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WeightRecordBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WeightRecord build() {
            return new WeightRecord(id, userId, recordId, weight, bodyFatPercentage, waistCircumference,
                    hipCircumference, chestCircumference, thighCircumference, measurementTimePeriod, note,
                    imageUrl, date, createdAt, updatedAt);
        }
    }
}

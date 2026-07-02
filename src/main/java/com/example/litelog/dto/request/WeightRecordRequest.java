package com.example.litelog.dto.request;

import jakarta.validation.constraints.NotNull;

public class WeightRecordRequest {

    @NotNull(message = "记录ID不能为空")
    private String recordId;

    @NotNull(message = "体重不能为空")
    private Double weight;

    private Double bodyFatPercentage;

    private Double waistCircumference;

    private Double hipCircumference;

    private Double chestCircumference;

    private Double thighCircumference;

    private String measurementTimePeriod;

    private String note;

    @NotNull(message = "日期不能为空")
    private Long date;

    @NotNull(message = "创建时间不能为空")
    private Long createdAt;

    @NotNull(message = "更新时间不能为空")
    private Long updatedAt;

    private Boolean deleted = false;

    private String imageUrl;

    private String imageFileName;

    private Boolean deleteImage = false;

    public WeightRecordRequest() {
    }

    public WeightRecordRequest(String recordId, Double weight, Double bodyFatPercentage, Double waistCircumference,
                               Double hipCircumference, Double chestCircumference, Double thighCircumference,
                               String measurementTimePeriod, String note, Long date, Long createdAt, Long updatedAt,
                               Boolean deleted, String imageUrl, String imageFileName) {
        this.recordId = recordId;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.waistCircumference = waistCircumference;
        this.hipCircumference = hipCircumference;
        this.chestCircumference = chestCircumference;
        this.thighCircumference = thighCircumference;
        this.measurementTimePeriod = measurementTimePeriod;
        this.note = note;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
        this.imageUrl = imageUrl;
        this.imageFileName = imageFileName;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Boolean getDeleteImage() {
        return deleteImage;
    }

    public void setDeleteImage(Boolean deleteImage) {
        this.deleteImage = deleteImage;
    }
}

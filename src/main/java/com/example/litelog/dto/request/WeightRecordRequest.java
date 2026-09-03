package com.example.litelog.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WeightRecordRequest {

    @NotNull(message = "记录ID不能为空")
    @Size(max = 100, message = "记录ID长度不能超过100个字符")
    private String recordId;

    @NotNull(message = "体重不能为空")
    @DecimalMin(value = "0", message = "体重不能小于0")
    @DecimalMax(value = "1000", message = "体重不能超过1000")
    private Double weight;

    @DecimalMin(value = "0", message = "体脂率不能小于0")
    @DecimalMax(value = "100", message = "体脂率不能超过100")
    private Double bodyFatPercentage;

    @DecimalMin(value = "0", message = "腰围不能小于0")
    @DecimalMax(value = "500", message = "腰围不能超过500")
    private Double waistCircumference;

    @DecimalMin(value = "0", message = "臀围不能小于0")
    @DecimalMax(value = "500", message = "臀围不能超过500")
    private Double hipCircumference;

    @DecimalMin(value = "0", message = "胸围不能小于0")
    @DecimalMax(value = "500", message = "胸围不能超过500")
    private Double chestCircumference;

    @DecimalMin(value = "0", message = "大腿围不能小于0")
    @DecimalMax(value = "500", message = "大腿围不能超过500")
    private Double thighCircumference;

    @Size(max = 50, message = "测量时间段长度不能超过50个字符")
    private String measurementTimePeriod;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String note;

    @NotNull(message = "日期不能为空")
    private Long date;

    @NotNull(message = "创建时间不能为空")
    private Long createdAt;

    @NotNull(message = "更新时间不能为空")
    private Long updatedAt;

    private Boolean deleted = false;

    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    private String imageUrl;

    @Size(max = 200, message = "图片文件名长度不能超过200个字符")
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

package com.example.litelog.dto.response;

import java.util.List;

/**
 * 获取用户所有数据的响应 DTO，替代 Map<String, Object>
 */
public class FetchAllDataResponse {

    private Boolean success;
    private String message;
    private UpdateProfileResponse profile;
    private List<RecordData> records;
    private Long totalRecords;
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;

    public static class RecordData {
        private String recordId;
        private Double weight;
        private Double bodyFatPercentage;
        private Double waistCircumference;
        private Double hipCircumference;
        private Double chestCircumference;
        private Double thighCircumference;
        private String note;
        private Long date;
        private Long createdAt;
        private Long updatedAt;
        private String imageUrl;
        private String measurementTimePeriod;

        public RecordData() {
        }

        public RecordData(String recordId, Double weight, Double bodyFatPercentage, Double waistCircumference,
                          Double hipCircumference, Double chestCircumference, Double thighCircumference,
                          String note, Long date, Long createdAt, Long updatedAt,
                          String imageUrl, String measurementTimePeriod) {
            this.recordId = recordId;
            this.weight = weight;
            this.bodyFatPercentage = bodyFatPercentage;
            this.waistCircumference = waistCircumference;
            this.hipCircumference = hipCircumference;
            this.chestCircumference = chestCircumference;
            this.thighCircumference = thighCircumference;
            this.note = note;
            this.date = date;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.imageUrl = imageUrl;
            this.measurementTimePeriod = measurementTimePeriod;
        }

        public String getRecordId() { return recordId; }
        public void setRecordId(String recordId) { this.recordId = recordId; }
        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }
        public Double getBodyFatPercentage() { return bodyFatPercentage; }
        public void setBodyFatPercentage(Double bodyFatPercentage) { this.bodyFatPercentage = bodyFatPercentage; }
        public Double getWaistCircumference() { return waistCircumference; }
        public void setWaistCircumference(Double waistCircumference) { this.waistCircumference = waistCircumference; }
        public Double getHipCircumference() { return hipCircumference; }
        public void setHipCircumference(Double hipCircumference) { this.hipCircumference = hipCircumference; }
        public Double getChestCircumference() { return chestCircumference; }
        public void setChestCircumference(Double chestCircumference) { this.chestCircumference = chestCircumference; }
        public Double getThighCircumference() { return thighCircumference; }
        public void setThighCircumference(Double thighCircumference) { this.thighCircumference = thighCircumference; }
        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
        public Long getDate() { return date; }
        public void setDate(Long date) { this.date = date; }
        public Long getCreatedAt() { return createdAt; }
        public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
        public Long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getMeasurementTimePeriod() { return measurementTimePeriod; }
        public void setMeasurementTimePeriod(String measurementTimePeriod) { this.measurementTimePeriod = measurementTimePeriod; }
    }

    public FetchAllDataResponse() {
    }

    public static FetchAllDataResponseBuilder builder() {
        return new FetchAllDataResponseBuilder();
    }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public UpdateProfileResponse getProfile() { return profile; }
    public void setProfile(UpdateProfileResponse profile) { this.profile = profile; }
    public List<RecordData> getRecords() { return records; }
    public void setRecords(List<RecordData> records) { this.records = records; }
    public Long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Long totalRecords) { this.totalRecords = totalRecords; }
    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    public static class FetchAllDataResponseBuilder {
        private Boolean success;
        private String message;
        private UpdateProfileResponse profile;
        private List<RecordData> records;
        private Long totalRecords;
        private Integer currentPage;
        private Integer totalPages;
        private Integer pageSize;

        public FetchAllDataResponseBuilder success(Boolean success) { this.success = success; return this; }
        public FetchAllDataResponseBuilder message(String message) { this.message = message; return this; }
        public FetchAllDataResponseBuilder profile(UpdateProfileResponse profile) { this.profile = profile; return this; }
        public FetchAllDataResponseBuilder records(List<RecordData> records) { this.records = records; return this; }
        public FetchAllDataResponseBuilder totalRecords(Long totalRecords) { this.totalRecords = totalRecords; return this; }
        public FetchAllDataResponseBuilder currentPage(Integer currentPage) { this.currentPage = currentPage; return this; }
        public FetchAllDataResponseBuilder totalPages(Integer totalPages) { this.totalPages = totalPages; return this; }
        public FetchAllDataResponseBuilder pageSize(Integer pageSize) { this.pageSize = pageSize; return this; }

        public FetchAllDataResponse build() {
            return new FetchAllDataResponse(success, message, profile, records, totalRecords, currentPage, totalPages, pageSize);
        }
    }

    private FetchAllDataResponse(Boolean success, String message, UpdateProfileResponse profile,
                                  List<RecordData> records, Long totalRecords, Integer currentPage,
                                  Integer totalPages, Integer pageSize) {
        this.success = success;
        this.message = message;
        this.profile = profile;
        this.records = records;
        this.totalRecords = totalRecords;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }
}
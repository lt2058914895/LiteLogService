package com.example.litelog.dto.request;

import java.util.List;

public class WeightRecordSyncWithImagesRequest {

    private String recordsJson;
    private List<org.springframework.web.multipart.MultipartFile> files;

    public WeightRecordSyncWithImagesRequest() {
    }

    public WeightRecordSyncWithImagesRequest(String recordsJson, List<org.springframework.web.multipart.MultipartFile> files) {
        this.recordsJson = recordsJson;
        this.files = files;
    }

    public String getRecordsJson() {
        return recordsJson;
    }

    public void setRecordsJson(String recordsJson) {
        this.recordsJson = recordsJson;
    }

    public List<org.springframework.web.multipart.MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<org.springframework.web.multipart.MultipartFile> files) {
        this.files = files;
    }

    public static WeightRecordSyncWithImagesRequestBuilder builder() {
        return new WeightRecordSyncWithImagesRequestBuilder();
    }

    public static class WeightRecordSyncWithImagesRequestBuilder {
        private String recordsJson;
        private List<org.springframework.web.multipart.MultipartFile> files;

        public WeightRecordSyncWithImagesRequestBuilder recordsJson(String recordsJson) {
            this.recordsJson = recordsJson;
            return this;
        }

        public WeightRecordSyncWithImagesRequestBuilder files(List<org.springframework.web.multipart.MultipartFile> files) {
            this.files = files;
            return this;
        }

        public WeightRecordSyncWithImagesRequest build() {
            return new WeightRecordSyncWithImagesRequest(recordsJson, files);
        }
    }
}
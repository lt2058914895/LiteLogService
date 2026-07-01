package com.example.litelog.dto.response;

import java.util.List;

public class WeightRecordSyncResponse {

    private boolean success;
    private String message;
    private int syncedCount;
    private List<String> syncedRecordIds;

    public WeightRecordSyncResponse() {
    }

    public WeightRecordSyncResponse(boolean success, String message, int syncedCount, List<String> syncedRecordIds) {
        this.success = success;
        this.message = message;
        this.syncedCount = syncedCount;
        this.syncedRecordIds = syncedRecordIds;
    }

    public static WeightRecordSyncResponseBuilder builder() {
        return new WeightRecordSyncResponseBuilder();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSyncedCount() {
        return syncedCount;
    }

    public void setSyncedCount(int syncedCount) {
        this.syncedCount = syncedCount;
    }

    public List<String> getSyncedRecordIds() {
        return syncedRecordIds;
    }

    public void setSyncedRecordIds(List<String> syncedRecordIds) {
        this.syncedRecordIds = syncedRecordIds;
    }

    public static class WeightRecordSyncResponseBuilder {
        private boolean success;
        private String message;
        private int syncedCount;
        private List<String> syncedRecordIds;

        public WeightRecordSyncResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public WeightRecordSyncResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public WeightRecordSyncResponseBuilder syncedCount(int syncedCount) {
            this.syncedCount = syncedCount;
            return this;
        }

        public WeightRecordSyncResponseBuilder syncedRecordIds(List<String> syncedRecordIds) {
            this.syncedRecordIds = syncedRecordIds;
            return this;
        }

        public WeightRecordSyncResponse build() {
            return new WeightRecordSyncResponse(success, message, syncedCount, syncedRecordIds);
        }
    }
}

package com.example.litelog.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class WeightRecordSyncRequest {

    @NotEmpty(message = "记录列表不能为空")
    @Valid
    private List<WeightRecordRequest> records;

    public WeightRecordSyncRequest() {
    }

    public WeightRecordSyncRequest(List<WeightRecordRequest> records) {
        this.records = records;
    }

    public List<WeightRecordRequest> getRecords() {
        return records;
    }

    public void setRecords(List<WeightRecordRequest> records) {
        this.records = records;
    }
}

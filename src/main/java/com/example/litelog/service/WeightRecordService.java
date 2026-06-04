package com.example.litelog.service;

import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;

public interface WeightRecordService {

    WeightRecordSyncResponse syncRecords(Long userId, WeightRecordSyncRequest request);
}
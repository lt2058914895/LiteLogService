package com.example.litelog.service;

import com.example.litelog.dto.request.WeightRecordSyncRequest;
import com.example.litelog.dto.response.WeightRecordSyncResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WeightRecordService {

    WeightRecordSyncResponse syncRecords(String userId, String idType, WeightRecordSyncRequest request);

    WeightRecordSyncResponse syncRecordsWithImages(String userId, String idType, WeightRecordSyncRequest request, List<MultipartFile> files);
}
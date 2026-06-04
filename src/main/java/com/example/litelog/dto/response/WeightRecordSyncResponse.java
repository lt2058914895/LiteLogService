package com.example.litelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordSyncResponse {

    private boolean success;
    private String message;
    private int syncedCount;
    private List<String> syncedRecordIds;
}
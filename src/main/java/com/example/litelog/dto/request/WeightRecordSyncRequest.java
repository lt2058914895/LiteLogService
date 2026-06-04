package com.example.litelog.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordSyncRequest {

    @NotEmpty(message = "记录列表不能为空")
    @Valid
    private List<WeightRecordRequest> records;
}
package com.example.litelog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordSyncWithImagesRequest {

    private String recordsJson;

    private List<org.springframework.web.multipart.MultipartFile> files;
}
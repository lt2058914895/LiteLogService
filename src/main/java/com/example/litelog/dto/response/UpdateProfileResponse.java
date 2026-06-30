package com.example.litelog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileResponse {

    private Boolean success;
    private String message;
    private String nickname;
    private String avatarUrl;
    private Double height;
    private Integer gender;
    private Integer age;
    private Double goalWeight;
    private Double goalBodyFat;
    private Double goalWaistCircumference;
    private Double goalHipCircumference;
    private Double goalChestCircumference;
    private Double goalThighCircumference;
    private String weightUnit;
}
package com.example.litelog.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordRequest {

    @NotNull(message = "记录ID不能为空")
    private String recordId;

    @NotNull(message = "体重不能为空")
    private Double weight;

    private Double bodyFatPercentage;

    private Double waistCircumference;

    private String note;

    @NotNull(message = "日期不能为空")
    private Long date;

    @NotNull(message = "创建时间不能为空")
    private Long createdAt;

    @NotNull(message = "更新时间不能为空")
    private Long updatedAt;

    /**
     * 是否已删除
     */
    @Builder.Default
    private Boolean deleted = false;
}
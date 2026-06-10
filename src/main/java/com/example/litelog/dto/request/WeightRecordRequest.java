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

    private Double hipCircumference;

    private Double thighCircumference;

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

    /**
     * 图片URL（已上传的图片路径）
     */
    private String imageUrl;

    /**
     * 图片文件名（用于匹配上传的文件）
     */
    private String imageFileName;
}
package com.example.litelog.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 1, max = 50, message = "昵称长度必须在1-50个字符之间")
    private String nickname;

    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatarUrl;

    @DecimalMin(value = "0", message = "身高不能小于0")
    @DecimalMax(value = "300", message = "身高不能超过300")
    private Double height;

    @DecimalMin(value = "0", message = "性别值不能小于0")
    @DecimalMax(value = "2", message = "性别值不能超过2")
    private Integer gender;

    @DecimalMin(value = "0", message = "年龄不能小于0")
    @DecimalMax(value = "200", message = "年龄不能超过200")
    private Integer age;

    @DecimalMin(value = "0", message = "目标体重不能小于0")
    @DecimalMax(value = "1000", message = "目标体重不能超过1000")
    private Double goalWeight;

    @DecimalMin(value = "0", message = "目标体脂率不能小于0")
    @DecimalMax(value = "100", message = "目标体脂率不能超过100")
    private Double goalBodyFat;

    @DecimalMin(value = "0", message = "目标腰围不能小于0")
    @DecimalMax(value = "500", message = "目标腰围不能超过500")
    private Double goalWaistCircumference;

    @DecimalMin(value = "0", message = "目标臀围不能小于0")
    @DecimalMax(value = "500", message = "目标臀围不能超过500")
    private Double goalHipCircumference;

    @DecimalMin(value = "0", message = "目标胸围不能小于0")
    @DecimalMax(value = "500", message = "目标胸围不能超过500")
    private Double goalChestCircumference;

    @DecimalMin(value = "0", message = "目标大腿围不能小于0")
    @DecimalMax(value = "500", message = "目标大腿围不能超过500")
    private Double goalThighCircumference;

    @Size(max = 10, message = "体重单位长度不能超过10个字符")
    private String weightUnit;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String nickname, String avatarUrl, Double height, Integer gender, Integer age,
                                Double goalWeight, Double goalBodyFat, Double goalWaistCircumference,
                                Double goalHipCircumference, Double goalChestCircumference, Double goalThighCircumference,
                                String weightUnit) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.height = height;
        this.gender = gender;
        this.age = age;
        this.goalWeight = goalWeight;
        this.goalBodyFat = goalBodyFat;
        this.goalWaistCircumference = goalWaistCircumference;
        this.goalHipCircumference = goalHipCircumference;
        this.goalChestCircumference = goalChestCircumference;
        this.goalThighCircumference = goalThighCircumference;
        this.weightUnit = weightUnit;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(Double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public Double getGoalBodyFat() {
        return goalBodyFat;
    }

    public void setGoalBodyFat(Double goalBodyFat) {
        this.goalBodyFat = goalBodyFat;
    }

    public Double getGoalWaistCircumference() {
        return goalWaistCircumference;
    }

    public void setGoalWaistCircumference(Double goalWaistCircumference) {
        this.goalWaistCircumference = goalWaistCircumference;
    }

    public Double getGoalHipCircumference() {
        return goalHipCircumference;
    }

    public void setGoalHipCircumference(Double goalHipCircumference) {
        this.goalHipCircumference = goalHipCircumference;
    }

    public Double getGoalChestCircumference() {
        return goalChestCircumference;
    }

    public void setGoalChestCircumference(Double goalChestCircumference) {
        this.goalChestCircumference = goalChestCircumference;
    }

    public Double getGoalThighCircumference() {
        return goalThighCircumference;
    }

    public void setGoalThighCircumference(Double goalThighCircumference) {
        this.goalThighCircumference = goalThighCircumference;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
}

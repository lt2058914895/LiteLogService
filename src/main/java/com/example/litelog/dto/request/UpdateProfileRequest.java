package com.example.litelog.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 1, max = 50, message = "昵称长度必须在1-50个字符之间")
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

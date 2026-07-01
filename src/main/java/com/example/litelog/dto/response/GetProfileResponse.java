package com.example.litelog.dto.response;

public class GetProfileResponse {

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

    public GetProfileResponse() {
    }

    public GetProfileResponse(Boolean success, String message, String nickname, String avatarUrl, Double height,
                             Integer gender, Integer age, Double goalWeight, Double goalBodyFat,
                             Double goalWaistCircumference, Double goalHipCircumference, Double goalChestCircumference,
                             Double goalThighCircumference, String weightUnit) {
        this.success = success;
        this.message = message;
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

    public static GetProfileResponseBuilder builder() {
        return new GetProfileResponseBuilder();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public static class GetProfileResponseBuilder {
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

        public GetProfileResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public GetProfileResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public GetProfileResponseBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public GetProfileResponseBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public GetProfileResponseBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public GetProfileResponseBuilder gender(Integer gender) {
            this.gender = gender;
            return this;
        }

        public GetProfileResponseBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public GetProfileResponseBuilder goalWeight(Double goalWeight) {
            this.goalWeight = goalWeight;
            return this;
        }

        public GetProfileResponseBuilder goalBodyFat(Double goalBodyFat) {
            this.goalBodyFat = goalBodyFat;
            return this;
        }

        public GetProfileResponseBuilder goalWaistCircumference(Double goalWaistCircumference) {
            this.goalWaistCircumference = goalWaistCircumference;
            return this;
        }

        public GetProfileResponseBuilder goalHipCircumference(Double goalHipCircumference) {
            this.goalHipCircumference = goalHipCircumference;
            return this;
        }

        public GetProfileResponseBuilder goalChestCircumference(Double goalChestCircumference) {
            this.goalChestCircumference = goalChestCircumference;
            return this;
        }

        public GetProfileResponseBuilder goalThighCircumference(Double goalThighCircumference) {
            this.goalThighCircumference = goalThighCircumference;
            return this;
        }

        public GetProfileResponseBuilder weightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
            return this;
        }

        public GetProfileResponse build() {
            return new GetProfileResponse(success, message, nickname, avatarUrl, height, gender, age, goalWeight,
                    goalBodyFat, goalWaistCircumference, goalHipCircumference, goalChestCircumference,
                    goalThighCircumference, weightUnit);
        }
    }
}

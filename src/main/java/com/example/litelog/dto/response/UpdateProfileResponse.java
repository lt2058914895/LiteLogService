package com.example.litelog.dto.response;

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

    public UpdateProfileResponse() {
    }

    public UpdateProfileResponse(Boolean success, String message, String nickname, String avatarUrl, Double height,
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

    public static UpdateProfileResponseBuilder builder() {
        return new UpdateProfileResponseBuilder();
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

    public static class UpdateProfileResponseBuilder {
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

        public UpdateProfileResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public UpdateProfileResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public UpdateProfileResponseBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UpdateProfileResponseBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public UpdateProfileResponseBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public UpdateProfileResponseBuilder gender(Integer gender) {
            this.gender = gender;
            return this;
        }

        public UpdateProfileResponseBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public UpdateProfileResponseBuilder goalWeight(Double goalWeight) {
            this.goalWeight = goalWeight;
            return this;
        }

        public UpdateProfileResponseBuilder goalBodyFat(Double goalBodyFat) {
            this.goalBodyFat = goalBodyFat;
            return this;
        }

        public UpdateProfileResponseBuilder goalWaistCircumference(Double goalWaistCircumference) {
            this.goalWaistCircumference = goalWaistCircumference;
            return this;
        }

        public UpdateProfileResponseBuilder goalHipCircumference(Double goalHipCircumference) {
            this.goalHipCircumference = goalHipCircumference;
            return this;
        }

        public UpdateProfileResponseBuilder goalChestCircumference(Double goalChestCircumference) {
            this.goalChestCircumference = goalChestCircumference;
            return this;
        }

        public UpdateProfileResponseBuilder goalThighCircumference(Double goalThighCircumference) {
            this.goalThighCircumference = goalThighCircumference;
            return this;
        }

        public UpdateProfileResponseBuilder weightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
            return this;
        }

        public UpdateProfileResponse build() {
            return new UpdateProfileResponse(success, message, nickname, avatarUrl, height, gender, age, goalWeight,
                    goalBodyFat, goalWaistCircumference, goalHipCircumference, goalChestCircumference,
                    goalThighCircumference, weightUnit);
        }
    }
}

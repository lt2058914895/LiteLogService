package com.example.litelog.service;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.UpdateProfileResponse;

public interface UserService {

    UpdateProfileResponse updateProfile(String userId, UpdateProfileRequest request);
}
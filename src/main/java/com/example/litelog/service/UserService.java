package com.example.litelog.service;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;

public interface UserService {

    UpdateProfileResponse updateProfile(String phone, UpdateProfileRequest request);

    GetProfileResponse getProfile(String phone);
}
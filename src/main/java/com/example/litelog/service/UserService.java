package com.example.litelog.service;

import com.example.litelog.dto.request.UpdateProfileRequest;
import com.example.litelog.dto.response.FetchAllDataResponse;
import com.example.litelog.dto.response.GetProfileResponse;
import com.example.litelog.dto.response.UpdateProfileResponse;

public interface UserService {

    UpdateProfileResponse updateProfile(String userId, String idType, UpdateProfileRequest request);

    GetProfileResponse getProfile(String userId, String idType);

    String uploadAvatar(String userId, String idType, byte[] imageData, String originalFilename);
    
    Long getOrCreateUserId(String userId, String idType);
    
    FetchAllDataResponse fetchAllData(String userId, String idType, Integer page, Integer size);
}
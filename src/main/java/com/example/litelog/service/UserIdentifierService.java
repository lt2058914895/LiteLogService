package com.example.litelog.service;

public interface UserIdentifierService {
    Long getOrCreateUserId(String userId, String idType);
}
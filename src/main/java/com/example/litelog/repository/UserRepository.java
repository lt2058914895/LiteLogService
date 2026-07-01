package com.example.litelog.repository;

import com.example.litelog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    boolean existsByPhone(String phone);
    Optional<User> findByAppleId(String appleId);
    boolean existsByAppleId(String appleId);
    
    Optional<User> findByDeviceId(String deviceId);
    boolean existsByDeviceId(String deviceId);
    
    Optional<User> findByCustomPhone(String customPhone);
    Optional<User> findByCustomEmail(String customEmail);
}
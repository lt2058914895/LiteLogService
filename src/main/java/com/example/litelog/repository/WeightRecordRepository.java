package com.example.litelog.repository;

import com.example.litelog.entity.WeightRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
    
    Optional<WeightRecord> findByRecordId(String recordId);
    
    List<WeightRecord> findByUserIdOrderByDateDesc(Long userId);
    
    List<WeightRecord> findByUserId(Long userId);
    
    Page<WeightRecord> findByUserId(Long userId, Pageable pageable);
    
    boolean existsByRecordId(String recordId);
    
    List<WeightRecord> findAllByRecordIdIn(List<String> recordIds);
}
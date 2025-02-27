package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}

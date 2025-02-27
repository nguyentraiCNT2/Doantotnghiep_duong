package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("select r FROM ReviewEntity r WHERE r.serviceId.id = :id ORDER BY r.createdAt desc ")
    List<ReviewEntity> findByServiceId(Long  id);

}

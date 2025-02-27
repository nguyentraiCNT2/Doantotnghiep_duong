package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("select r from RoomEntity r WHERE r.customer.id = :customerId")
    RoomEntity findByCustomer(@Param("customerId") Long customer);
    @Query("select r from RoomEntity r ORDER BY r.updatedAt desc ")
    List<RoomEntity> findAllRoom();
}

package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    boolean existsByNameAndDeleteAtIsNull(String name);

}

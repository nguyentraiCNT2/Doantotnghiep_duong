package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    @Query("select a from AppointmentEntity a WHERE a.userId.id = :userId")
    Page<AppointmentEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT m FROM AppointmentEntity m WHERE " +
            "(:fullName IS NULL OR m.userId.fullName LIKE %:fullName%) AND " +
            "(:serviceName IS NULL OR m.serviceId.name LIKE %:serviceName%) AND " +
            "(:appointmentDate IS NULL OR m.appointmentDate = :appointmentDate) AND " +
            "(:status is null OR m.status = :status)")
    Page<AppointmentEntity> filterAppointment(
            @Param("fullName") String fullName,
            @Param("serviceName") String serviceName,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("status") String status,
            Pageable pageable);

}

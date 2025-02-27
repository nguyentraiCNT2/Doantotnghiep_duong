package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;

public interface AppointmentService {
    Page<AppointmentDTO> getAllAppointment(String  userFullName, String serviceName, String status, LocalDate appointmentDate, Pageable pageable);
    Page<AppointmentDTO> getByUser(Long userId, Pageable pageable);
    Page<AppointmentDTO> getByMe( Pageable pageable);
    AppointmentDTO getById(Long id);
    AppointmentDTO save(AppointmentDTO appointmentDTO);
    AppointmentDTO update(AppointmentDTO appointmentDTO);
    void huyHen(Long id);

}

package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.AppointmentDTO;
import com.nckhntu.doantonghiep.Entity.AppointmentEntity;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.AppointmentRepository;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentIMPL implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private  final ModelMapper modelMapper;
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ServiceRepository serviceRepository;
    public AppointmentIMPL(AppointmentRepository appointmentRepository, ModelMapper modelMapper, HttpSession httpSession, UserRepository userRepository, PetRepository petRepository, ServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
        this.httpSession = httpSession;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<AppointmentDTO> getAllAppointment(String fullName, String serviceName, String status, LocalDate appointmentDate, Pageable pageable) {
        try {
            List<AppointmentDTO> appointmentEntities = new ArrayList<>();
            Page<AppointmentEntity> appointmentEntityPage = appointmentRepository.filterAppointment(fullName, serviceName, appointmentDate, status, pageable);
            appointmentEntityPage.getContent().forEach(appointmentEntity -> appointmentEntities.add(modelMapper.map(appointmentEntity, AppointmentDTO.class)));

            return new PageImpl<>(appointmentEntities, pageable, appointmentEntityPage.getTotalElements());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Page<AppointmentDTO> getByUser(Long userId, Pageable pageable) {
        try {
            List<AppointmentDTO> appointmentEntities = new ArrayList<>();
            Page<AppointmentEntity> appointmentEntityPage = appointmentRepository.findByUserId(userId,pageable);
            appointmentEntityPage.getContent().forEach(appointmentEntity -> appointmentEntities.add(modelMapper.map(appointmentEntity, AppointmentDTO.class)));
            return new PageImpl<>(appointmentEntities, pageable, appointmentEntityPage.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<AppointmentDTO> getByMe(Pageable pageable) {
            try {
                String userEmail = (String) httpSession.getAttribute("userEmail");
                UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

                List<AppointmentDTO> appointmentEntities = new ArrayList<>();
            Page<AppointmentEntity> appointmentEntityPage = appointmentRepository.findByUserId(userEntity.getId(),pageable);
            appointmentEntityPage.getContent().forEach(appointmentEntity -> appointmentEntities.add(modelMapper.map(appointmentEntity, AppointmentDTO.class)));
            return new PageImpl<>(appointmentEntities, pageable, appointmentEntityPage.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppointmentDTO getById(Long id) {
        try {
            AppointmentEntity appointment = appointmentRepository.findById(id).get();
            return modelMapper.map(appointment, AppointmentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        try {
            String userEmail = (String) httpSession.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

            PetEntity  pet = petRepository.findById(appointmentDTO.getPetId().getId()).get();
            ServiceEntity serviceEntity = serviceRepository.findById(appointmentDTO.getServiceId().getId()).get();
            AppointmentEntity appointmentEntity = modelMapper.map(appointmentDTO, AppointmentEntity.class);
            appointmentEntity.setStatus("PENDING");
            appointmentEntity.setPetId(pet);
            appointmentEntity.setServiceId(serviceEntity);
            appointmentEntity.setUserId(userEntity);
            appointmentEntity=      appointmentRepository.save(appointmentEntity);
            return modelMapper.map(appointmentEntity, AppointmentDTO.class);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        try {
            AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentDTO.getId()).get();
            appointmentEntity.setStatus(appointmentDTO.getStatus());
            appointmentEntity= appointmentRepository.save(appointmentEntity);
            return modelMapper.map(appointmentEntity, AppointmentDTO.class);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void huyHen(Long id) {
        try {
            AppointmentEntity appointmentEntity = appointmentRepository.findById(id).get();
            appointmentEntity.setStatus("Cancelled");
            appointmentRepository.save(appointmentEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

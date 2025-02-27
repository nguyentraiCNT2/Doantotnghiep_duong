package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Entity.LogEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.LogRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceIMPL implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    public ServiceIMPL(ServiceRepository serviceRepository, ModelMapper modelMapper, LogRepository logRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }



    @Override
    public ServiceDTO getServiceById(Long id) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dịch dụ nào tương tự"));
            ServiceDTO dto = modelMapper.map(serviceEntity, ServiceDTO.class);
            dto.setImage(null);

            dto.setImageBase64(serviceEntity.getImage() != null ? "/image/service/" + serviceEntity.getId() : null);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Page<ServiceDTO> getAllService(Pageable pageable) {
        Page<ServiceEntity> entities = serviceRepository.findAll(pageable);
        List<ServiceDTO> dtoList = entities.stream()
                .filter(entity -> entity.getDeleteAt() == null)
                .map(entity -> {
                    ServiceDTO dto = modelMapper.map(entity, ServiceDTO.class);
                    dto.setImage(null);

                    dto.setImageBase64(entity.getImage() != null ? "/image/service/" + entity.getId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, entities.getTotalElements());
    }

    @Override
    public List<ServiceDTO> getAllService() {
        List<ServiceEntity> entities = serviceRepository.findAll();
        List<ServiceDTO> dtoList = entities.stream()
                .filter(entity -> entity.getDeleteAt() == null)
                .map(entity -> {
                    ServiceDTO dto = modelMapper.map(entity, ServiceDTO.class);
                    dto.setImage(null);

                    dto.setImageBase64(entity.getImage() != null ? "/image/service/" + entity.getId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public ServiceDTO addService(ServiceDTO serviceDTO, MultipartFile imageFile) {
        try {
            ServiceEntity serviceEntity = modelMapper.map(serviceDTO, ServiceEntity.class);
            if (imageFile != null && !imageFile.isEmpty()) {
                serviceEntity.setImage(imageFile.getBytes());
            }
            serviceEntity = serviceRepository.save(serviceEntity);
            return modelMapper.map(serviceEntity, ServiceDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xử lý ảnh: " + e.getMessage());
        }
    }

    @Override
    public ServiceDTO updateService(ServiceDTO serviceDTO, MultipartFile imageFile) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(serviceDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));
            serviceEntity.setName(serviceDTO.getName());
            serviceEntity.setDescription(serviceDTO.getDescription());
            serviceEntity.setDuration(serviceDTO.getDuration());
            if (imageFile != null && !imageFile.isEmpty()) {
                serviceEntity.setImage(imageFile.getBytes());
            }
            serviceRepository.save(serviceEntity);
            return modelMapper.map(serviceEntity, ServiceDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi cập nhật ảnh: " + e.getMessage());
        }
    }

    @Override
    public void deleteService(Long id) {
        try {
            ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ nào tương tự"));
            serviceEntity.setDeleteAt(Timestamp.from(Instant.now()));
            serviceRepository.save(serviceEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

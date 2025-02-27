package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServiceService {
    Page<ServiceDTO> getAllService(Pageable pageable);
    List<ServiceDTO> getAllService();
    ServiceDTO getServiceById(Long id);
    ServiceDTO addService(ServiceDTO serviceDTO, MultipartFile imageFile);
    ServiceDTO updateService(ServiceDTO serviceDTO, MultipartFile imageFile);
    void deleteService(Long id);
}

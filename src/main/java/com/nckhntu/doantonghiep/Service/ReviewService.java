package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    Page<ReviewDTO> getAll(Pageable pageable);
    List<ReviewDTO> getByServiceId(Long serviceId);
     void save(ReviewDTO reviewDTO);

}

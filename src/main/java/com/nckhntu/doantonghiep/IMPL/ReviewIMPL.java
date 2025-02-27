package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.DTO.ReviewDTO;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.ReviewEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.ReviewRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewIMPL implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final HttpSession session;
    private final ServiceRepository serviceRepository;

    public ReviewIMPL(ReviewRepository reviewRepository, UserRepository userRepository, ModelMapper modelMapper, HttpSession session, ServiceRepository serviceRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.session = session;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<ReviewDTO> getAll(Pageable pageable) {
        try {
            Page<ReviewEntity> reviewEntities = reviewRepository.findAll(pageable);
            List<ReviewDTO> reviewDTOS = new ArrayList<>();
            reviewEntities.getContent().stream()
                    .forEach(reviewEntity -> {
                        ReviewDTO reviewDTO = modelMapper.map(reviewEntity, ReviewDTO.class);

                        reviewDTOS.add(reviewDTO);
                    });

            return new PageImpl<>(reviewDTOS, pageable, reviewEntities.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ReviewDTO> getByServiceId(Long serviceId) {
        try {
            List<ReviewEntity> reviewEntities = reviewRepository.findByServiceId(serviceId);
            List<ReviewDTO> reviewDTOS = new ArrayList<>();
            reviewEntities.stream()
                    .forEach(reviewEntity -> {
                        ReviewDTO reviewDTO = modelMapper.map(reviewEntity, ReviewDTO.class);
                        reviewDTOS.add(reviewDTO);
                    });

            return reviewDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(ReviewDTO reviewDTO) {
        try {
            String userName = (String) session.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userName).get();
            ServiceEntity serviceEntity = serviceRepository.findById(reviewDTO.getServiceId().getId()).get();
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setContent(reviewDTO.getContent());
            reviewEntity.setUserId(userEntity);
            reviewEntity.setServiceId(serviceEntity);
            reviewEntity.setRating(reviewDTO.getRating() != 0 ? reviewDTO.getRating() : 1);
            reviewEntity.setCreatedAt(Timestamp.from(Instant.now()));
            reviewRepository.save(reviewEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

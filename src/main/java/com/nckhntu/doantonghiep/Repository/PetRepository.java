package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.PetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    // Tìm kiếm theo tên (chứa từ khóa)
    Page<PetEntity> findByNameContaining(String name, Pageable pageable);

    // Tìm theo loài (species)
    Page<PetEntity> findBySpecies(String species, Pageable pageable);

    // Tìm theo giống (breed)
    Page<PetEntity> findByBreed(String breed, Pageable pageable);

    // Lọc theo độ tuổi
    Page<PetEntity> findByAgeBetween(int minAge, int maxAge, Pageable pageable);

    // Lọc theo trạng thái tiêm chủng
    Page<PetEntity> findByVaccinationStatus(String vaccinationStatus, Pageable pageable);
}

package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetService {
    Page<PetDTO> getAllPets(Pageable pageable);
    List<PetDTO> getAllPets();
    Page<PetDTO> searchPets(String name, String species, String breed, Integer minAge, Integer maxAge, String vaccinationStatus, Pageable pageable);
    PetDTO getPetById(Long id);
    PetDTO createPet(PetDTO petDTO, MultipartFile image);
    PetDTO updatePet(Long id, PetDTO petDTO, MultipartFile image);
    void deletePet(Long id);
}

package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper modelMapper;

    private PetDTO convertToDTO(PetEntity pet) {
        return modelMapper.map(pet, PetDTO.class);
    }

    private PetEntity convertToEntity(PetDTO petDTO) {
        return modelMapper.map(petDTO, PetEntity.class);
    }

    @Override
    public Page<PetDTO> getAllPets(Pageable pageable) {
        Page<PetEntity> pets = petRepository.findAll(pageable);
        List<PetDTO> petDTOs = new ArrayList<>();
        pets.getContent().stream()
                .filter(petEntity -> petEntity.getDeleteAt() == null)
                .forEach(petEntity -> {
                    PetDTO petDTO = modelMapper.map(petEntity, PetDTO.class);
                    petDTO.setImage(null);
                    petDTO.setImageBase64(petEntity.getImage() != null ? "/image/pet/" + petEntity.getId() : null);
                    petDTOs.add(petDTO);
                });

        return new PageImpl<>(petDTOs, pageable, pets.getTotalElements());
    }

    @Override
    public List<PetDTO> getAllPets() {
        try {
            List<PetEntity> pets = petRepository.findAll();
            List<PetDTO> petDTOs = new ArrayList<>();
            pets.stream()
                    .filter(petEntity -> petEntity.getDeleteAt() == null)
                    .forEach(petEntity -> {
                        PetDTO petDTO = modelMapper.map(petEntity, PetDTO.class);
                        petDTO.setImage(null);
                        petDTO.setImageBase64(petEntity.getImage() != null ? "/image/pet/" + petEntity.getId() : null);
                        petDTOs.add(petDTO);
                    });
            return petDTOs;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<PetDTO> searchPets(String name, String species, String breed, Integer minAge, Integer maxAge, String vaccinationStatus, Pageable pageable) {
       try {
           Page<PetEntity> pets;
           if (name != null) {
               pets = petRepository.findByNameContaining(name, pageable);
           } else if (species != null) {
               pets = petRepository.findBySpecies(species, pageable);
           } else if (breed != null) {
               pets = petRepository.findByBreed(breed, pageable);
           } else if (minAge != null && maxAge != null) {
               pets = petRepository.findByAgeBetween(minAge, maxAge, pageable);
           } else if (vaccinationStatus != null) {
               pets = petRepository.findByVaccinationStatus(vaccinationStatus, pageable);
           } else {
               pets = petRepository.findAll(pageable);
           }
           List<PetDTO> petDTOs = new ArrayList<>();
            pets.getContent().stream()
                    .filter(petEntity -> petEntity.getDeleteAt() == null)
                    .forEach(petEntity -> {
                        PetDTO petDTO = modelMapper.map(petEntity, PetDTO.class);
                        petDTO.setImage(null);
                        petDTO.setImageBase64(petEntity.getImage() != null ? "/image/pet/" + petEntity.getId() : null);
                        petDTOs.add(petDTO);
                    });

           return new PageImpl<>(petDTOs, pageable, pets.getTotalElements());
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
       }

    }

    @Override
    public PetDTO getPetById(Long id) {
        try {
            PetEntity pet = petRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thú cưng"));
            PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
            petDTO.setImage(null);

            petDTO.setImageBase64(pet.getImage() != null ? "/image/pet/" + pet.getId() : null);

            return petDTO;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PetDTO createPet(PetDTO petDTO, MultipartFile image) {
        try {
            PetEntity pet = convertToEntity(petDTO);
            pet.setCreateAt(Timestamp.from(Instant.now()));
            if (image != null && !image.isEmpty()) {
                pet.setImage(image.getBytes());
            }
            pet = petRepository.save(pet);
            return convertToDTO(pet);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public PetDTO updatePet(Long id, PetDTO petDTO, MultipartFile image) {
        try {
            Optional<PetEntity> optionalPet = petRepository.findById(id);
            if (optionalPet.isPresent()) {
                PetEntity pet = optionalPet.get();
                pet.setName(petDTO.getName());
                pet.setSpecies(petDTO.getSpecies());
                pet.setBreed(petDTO.getBreed());
                pet.setPrice(petDTO.getPrice());
                pet.setAge(petDTO.getAge());
                pet.setWeight(petDTO.getWeight());
                pet.setHealthNotes(petDTO.getHealthNotes());
                pet.setUpdateAt(Timestamp.from(Instant.now()));
                pet.setVaccinationStatus(petDTO.getVaccinationStatus());
                if (image != null && !image.isEmpty()) {
                    pet.setImage(image.getBytes());
                }
                return convertToDTO(petRepository.save(pet));
            } else {
                throw new RuntimeException("Không tìm thấy thú cưng để cập nhật");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deletePet(Long id) {
        try {
            PetEntity pet = petRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thú cưng"));
            pet.setDeleteAt(Timestamp.from(Instant.now()));
            petRepository.save(pet);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

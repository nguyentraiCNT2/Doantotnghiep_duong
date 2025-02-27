package com.nckhntu.doantonghiep.Controller;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Service.PetService;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    private final PetService petService;
    private final ServiceService serviceService;
    private final PetRepository petRepository;
    private final ServiceRepository serviceRepository;

    public HomeController(PetService petService, ServiceService serviceService, PetRepository petRepository, ServiceRepository serviceRepository) {
        this.petService = petService;
        this.serviceService = serviceService;
        this.petRepository = petRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<PetDTO> petDTOList = petService.getAllPets();
        List<ServiceDTO> dtoList = serviceService.getAllService();
        model.addAttribute("pets", petDTOList);
        model.addAttribute("services", dtoList);
        return "home";
    }
    @GetMapping("/pets/image/{id}")
    public ResponseEntity<byte[]> getImagePets(@PathVariable Long id) {
        Optional<PetEntity> serviceOpt = petRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Hoặc IMAGE_JPEG tùy loại ảnh
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @CrossOrigin
    @GetMapping("/services/image/{id}")
    public ResponseEntity<byte[]> getImageService(@PathVariable Long id) {
        Optional<ServiceEntity> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

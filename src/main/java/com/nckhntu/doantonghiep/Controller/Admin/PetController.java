package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/admin/pets")
public class PetController {

    private final PetService petService;
    private final PetRepository petRepository;

    public PetController(PetRepository petRepository, PetService petService) {
        this.petRepository = petRepository;
        this.petService = petService;
    }

    // 📌 Hiển thị danh sách thú cưng với phân trang và bộ lọc
    @GetMapping("/list")
    public String listPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String vaccinationStatus,
            Model model) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PetDTO> pets = petService.searchPets(name, species, breed, minAge, maxAge, vaccinationStatus, pageable);
            model.addAttribute("pets", pets.getContent());
            model.addAttribute("totalPages", pets.getTotalPages());
            model.addAttribute("currentPage", page);
            return "Admin/Pets/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Pets/list";
        }

    }

    // 📌 Hiển thị trang thêm thú cưng
    @GetMapping("/detail/{id}")
    public String showAddForm(@PathVariable Long id, Model model) {
        try {
            PetDTO dto = petService.getPetById(id);
            model.addAttribute("pet", dto);
            return "Admin/Pets/add";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    // 📌 Hiển thị trang thêm thú cưng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("pet", new PetDTO());
        return "Admin/Pets/add";
    }

    // 📌 Xử lý thêm thú cưng
    @PostMapping("/add")
    public String addPet(
            @ModelAttribute PetDTO petDTO,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, Model model) {
        try {
            petService.createPet(petDTO, imageFile);
            return "redirect:/admin/pets/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Pets/list";
        }
    }

    // 📌 Hiển thị trang cập nhật thú cưng
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PetDTO pet = petService.getPetById(id);
        model.addAttribute("pet", pet);
        return "Admin/Pets/edit";
    }

    // 📌 Xử lý cập nhật thú cưng
    @PostMapping("/edit/{id}")
    public String updatePet(
            @PathVariable Long id,
            @ModelAttribute PetDTO petDTO,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, Model model) {
        try {
            petService.updatePet(id, petDTO, imageFile);
            return "redirect:/admin/pets/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Pets/edit";
        }

    }

    // 📌 Xóa thú cưng (đánh dấu xóa)
    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id, Model model) {
        try {
            petService.deletePet(id);
            return "redirect:/admin/pets/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Pets/list";
        }

    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<PetEntity> serviceOpt = petRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Hoặc IMAGE_JPEG tùy loại ảnh
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

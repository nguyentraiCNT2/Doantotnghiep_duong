package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.Service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user/pets")
public class UserPetController {
    private final PetService petService;

    public UserPetController(PetService petService) {
        this.petService = petService;
    }

    // 📌 Hiển thị danh sách thú cưng với phân trang và bộ lọc
    @GetMapping("/list")
    public String listPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
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
            return "list-pets";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "list-pets";
        }

    }

    // 📌 Hiển thị trang thêm thú cưng
    @GetMapping("/detail/{id}")
    public String showAddForm(@PathVariable Long id, Model model) {
        try {
            PetDTO dto = petService.getPetById(id);
            model.addAttribute("pet", dto);
            return "Pet-detail";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    // 📌 Hiển thị trang thêm thú cưng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("pet", new PetDTO());
        return "add-pet";
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
            return "add-pet";
        }
    }

}

package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.UserBuyPetDTO;
import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Service.PetService;
import com.nckhntu.doantonghiep.Service.UserByPetService;
import com.nckhntu.doantonghiep.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/user-buy-pets")
public class UserBuyPetController {

    private final UserByPetService userByPetService;
    private final UserService userService;
    private final PetService petService;

    public UserBuyPetController(PetService petService, UserService userService, UserByPetService userByPetService) {
        this.petService = petService;
        this.userService = userService;
        this.userByPetService = userByPetService;
    }

    // Hiển thị danh sách phân trang
    @GetMapping
    public String getAllUserBuyPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userDTOPage = userService.getAll(pageable);
        model.addAttribute("userDTOPage", userDTOPage.getContent());
        model.addAttribute("totalPages", userDTOPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "Admin/UserBuyPet/list"; // Trả về view để hiển thị danh sách
    }

    // Hiển thị danh sách theo người dùng hiện tại
    @GetMapping("/by-user/{id}")
    public String getAllUserBuyPetsByUser(@PathVariable Long id, Model model) {
        List<UserBuyPetDTO> userBuyPets = userByPetService.getAllUserBuyPetByUserId(id);
        model.addAttribute("userBuyPets", userBuyPets);
        return "Admin/UserBuyPet/list-pet"; // Trả về view để hiển thị danh sách
    }

    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("userBuyPetDTO", new UserBuyPetDTO());
        model.addAttribute("userDTO",userService.getAllUserDtos());
        model.addAttribute("petList",petService.getAllPets());
        return "Admin/UserBuyPet/add"; // Trả về view form thêm mới
    }

    // Xử lý thêm mới
    @PostMapping("/add")
    public String addPetForUser(@ModelAttribute UserBuyPetDTO userBuyPetDTO) {
        userByPetService.addPetForUser(userBuyPetDTO);
        return "redirect:/admin/user-buy-pets"; // Chuyển hướng về trang danh sách
    }

    // Hiển thị form cập nhật
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Lấy thông tin UserBuyPetDTO từ service (giả sử có phương thức getById)
        UserBuyPetDTO userBuyPetDTO = userByPetService.getUserBuyPetById(id);
        model.addAttribute("userBuyPetDTO", userBuyPetDTO);
        model.addAttribute("userDTO",userService.getAllUserDtos());
        model.addAttribute("petList",petService.getAllPets());
        return "Admin/UserBuyPet/edit"; // Trả về view form cập nhật
    }

    // Xử lý cập nhật
    @PostMapping("/edit/{id}")
    public String updatePetForUser(@PathVariable Long id, @ModelAttribute UserBuyPetDTO userBuyPetDTO) {
        userBuyPetDTO.setId(id);
        userByPetService.updatePetForUser(userBuyPetDTO);
        return "redirect:/admin/user-buy-pets"; // Chuyển hướng về trang danh sách
    }


}
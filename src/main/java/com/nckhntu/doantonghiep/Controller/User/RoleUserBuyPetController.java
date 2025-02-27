package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.UserBuyPetDTO;
import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Service.UserByPetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/buy-pet")
public class RoleUserBuyPetController {
    private final UserByPetService userByPetService;

    public RoleUserBuyPetController(UserByPetService userByPetService) {
        this.userByPetService = userByPetService;
    }

    @GetMapping
    public String getAllUserBuyPets(
            Model model) {
        List<UserBuyPetDTO> userBuyPetDTOS = userByPetService.getAllUserBuyPetByUser();
        model.addAttribute("userBuyPetDTOS", userBuyPetDTOS);
        return "Profiles/User-List-pet-buyed"; // Trả về view để hiển thị danh sách
    }
}

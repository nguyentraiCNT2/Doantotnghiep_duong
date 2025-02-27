package com.nckhntu.doantonghiep.Controller;

import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Request.ChangePasswordRequest;
import com.nckhntu.doantonghiep.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            UserDTO dto = userService.me();
            model.addAttribute("user", dto);
            return "Profiles/Profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/Profile";
        }
    }

    @GetMapping("/profile/update")
    public String updateProfile(Model model) {
        try {
            UserDTO dto = userService.me();
            model.addAttribute("user", dto);
            return "Profiles/Update-profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/Update-profile";
        }
    }
    @PostMapping("/profile/update")
    public String updateProfile(Model model, @ModelAttribute UserDTO dto) {
        try {
             userService.updateProfile(dto);
            model.addAttribute("message", "Cập nhật thông tin thành công");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/Update-profile";
        }
    }

    @GetMapping("/profile/change-password")
    public String changePassword(Model model) {
        try {
            return "Profiles/ChangePassword";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/ChangePassword";
        }
    }
    @PostMapping("/profile/change-password")
    public String updateProfile(Model model, @ModelAttribute ChangePasswordRequest request) {
        try {
            userService.updatePassword(request.getOldPassword(), request.getNewPassword(), request.getConfirmPassword());
            model.addAttribute("message", "Cập nhật mật khẩu thành công");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/ChangePassword";
        }
    }
    @PostMapping("/change-image")
    public String changeImage(@RequestParam("imageFile")MultipartFile imageFile, Model model) {
        try {
             userService.changeImage(imageFile);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/Profile";
        }
    }
}

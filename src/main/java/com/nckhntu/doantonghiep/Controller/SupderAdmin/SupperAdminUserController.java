package com.nckhntu.doantonghiep.Controller.SupderAdmin;

import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/superadmin/user")
public class SupperAdminUserController {
    private final UserService userService;

    public SupperAdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAll(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserDTO> userDTOPage = userService.getAll(pageable);
            model.addAttribute("userDTOPage", userDTOPage.getContent());
            model.addAttribute("totalPages", userDTOPage.getTotalPages());
            model.addAttribute("currentPage", page);
            return "Admin/Users/List-User";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Users/List-User";
        }
    }
    @GetMapping("/detail/{id}")
    public String getById(@PathVariable Long id, Model model) {
        try {
            UserDTO userDTO = userService.getById(id);
            model.addAttribute("user", userDTO);
            return "Admin/Users/detaill";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Users/detaill";
        }
    }

    @GetMapping("/updateRole/{id}")
    public String updateRole(@PathVariable Long id, Model model) {
        try {
            UserDTO userDTO = userService.getById(id);
            model.addAttribute("user", userDTO);
            return "Admin/Users/detaill";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Users/detaill";
        }
    }
    // Cập nhật vai trò người dùng (ví dụ: từ form)
    // POST /admin/users/{id}/updateRole?role=ADMIN
    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable("id") Long userId,
                             @RequestParam("role") String role,
                             Model redirectAttributes) {
        try {
            userService.updateRole(userId, role);
            redirectAttributes.addAttribute("message", "Cập nhật vai trò thành công.");
            return "redirect:/superadmin/user";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/superadmin/user"; // Chuyển hướng về trang danh sách người dùng

        }
    }

    // Hủy kích hoạt người dùng (Unactive)
    // POST /admin/users/{id}/deactivate
    @GetMapping("/deactivate/{id}")
    public String unActiveUser(@PathVariable("id") Long userId,
                               Model redirectAttributes) {
        try {
            userService.unActiveUser(userId);
            redirectAttributes.addAttribute("message", "Người dùng đã bị hủy kích hoạt.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/user";
    }

    // Kích hoạt người dùng (Active)
    // POST /admin/users/{id}/activate
    @GetMapping("/activate/{id}")
    public String activeUser(@PathVariable("id") Long userId,
                             Model redirectAttributes) {
        try {
            userService.activeUser(userId);
            redirectAttributes.addAttribute("message", "Người dùng đã được kích hoạt.");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/user";
    }

}

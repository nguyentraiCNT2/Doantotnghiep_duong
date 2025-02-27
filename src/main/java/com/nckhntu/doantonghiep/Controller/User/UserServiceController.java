package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.ReviewDTO;
import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Service.ReviewService;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/service")
public class UserServiceController {
    private final ServiceService serviceService;
    private final ReviewService reviewService;

    public UserServiceController(ServiceService serviceService, ReviewService reviewService) {
        this.serviceService = serviceService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String listServices(Model model,
                               @RequestParam(defaultValue = "0") int page,  // Trang mặc định là 0
                               @RequestParam(defaultValue = "20") int size) { // Mặc định mỗi trang 20 dịch vụ
        try {
            Page<ServiceDTO> servicePage = serviceService.getAllService(PageRequest.of(page, size));
            model.addAttribute("services", servicePage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", servicePage.getTotalPages());
            return "list-service"; // Đường dẫn đến template Thymeleaf
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "list-service"; // Đường dẫn đến template Thymeleaf
        }

    }

    @GetMapping("/detail/{id}")
    public String listServices(@PathVariable Long id, Model model) { // Mặc định mỗi trang 20 dịch vụ
        try {
            ServiceDTO service = serviceService.getServiceById(id);
            List<ReviewDTO> reviewDTOS = reviewService.getByServiceId(id);
            model.addAttribute("service",service);
            model.addAttribute("reviews",reviewDTOS);
            return "Service-detail"; // Đường dẫn đến template Thymeleaf
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Service-detail"; // Đường dẫn đến template Thymeleaf
        }

    }
}

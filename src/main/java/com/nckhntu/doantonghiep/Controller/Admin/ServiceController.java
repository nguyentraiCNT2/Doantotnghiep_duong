package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/services")
public class ServiceController {
    private final ServiceService serviceService;
    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceService serviceService, ServiceRepository serviceRepository) {
        this.serviceService = serviceService;
        this.serviceRepository = serviceRepository;
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
            return "Admin/Service/list"; // Đường dẫn đến template Thymeleaf
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Service/list"; // Đường dẫn đến template Thymeleaf
        }

    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new ServiceDTO());
        return "Admin/Service/add";
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceDTO serviceDTO, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        try {
            serviceService.addService(serviceDTO, imageFile);
            return "redirect:/admin/services";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Service/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ServiceDTO service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "Admin/Service/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable Long id, @ModelAttribute ServiceDTO serviceDTO, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        try {
            serviceDTO.setId(id);
            serviceService.updateService(serviceDTO, imageFile);
            return "redirect:/admin/services";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/Service/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return "redirect:/admin/services";
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<ServiceEntity> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Hoặc IMAGE_JPEG tùy loại ảnh
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

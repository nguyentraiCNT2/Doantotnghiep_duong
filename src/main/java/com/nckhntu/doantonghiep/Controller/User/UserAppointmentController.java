package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.AppointmentDTO;
import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Service.AppointmentService;
import com.nckhntu.doantonghiep.Service.PetService;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/appointment")
public class UserAppointmentController {
    private final AppointmentService appointmentService;
    private final PetService petService;
    private final ServiceService serviceService;

    public UserAppointmentController(AppointmentService appointmentService, PetService petService, ServiceService serviceService) {
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.serviceService = serviceService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<PetDTO> petDTOList = petService.getAllPets();
        List<ServiceDTO> entities = serviceService.getAllService();
        model.addAttribute("appointment", new AppointmentDTO());
        model.addAttribute("pets", petDTOList);
        model.addAttribute("services", entities);
        return "create-appointments";
    }

    @PostMapping("/new")
    public String createAppointment(@ModelAttribute AppointmentDTO appointmentDTO, Model model) {
        appointmentService.save(appointmentDTO);
        model.addAttribute("message", "Đặt lịch hẹn thành công");
        return "create-appointments";
    }
    @GetMapping("/me")
    public String getMyAppointments(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appointments = appointmentService.getByMe(pageable);
        model.addAttribute("appointments", appointments.getContent());
        model.addAttribute("totalPages", appointments.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "Profiles/UserAppointmentsList";
    }
    @PostMapping("/cancel/{id}")
    public String CancelledAppointments(@PathVariable Long id, Model model){
        try {
            appointmentService.huyHen(id);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Profiles/UserAppointmentsList";
        }
    }
}

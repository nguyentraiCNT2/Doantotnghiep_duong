package com.nckhntu.doantonghiep.Controller.SupderAdmin;

import com.nckhntu.doantonghiep.DTO.AppointmentDTO;
import com.nckhntu.doantonghiep.DTO.PetDTO;
import com.nckhntu.doantonghiep.DTO.ServiceDTO;
import com.nckhntu.doantonghiep.Service.AppointmentService;
import com.nckhntu.doantonghiep.Service.PetService;
import com.nckhntu.doantonghiep.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/superadmin/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PetService petService;
    private final ServiceService serviceService;

    public AppointmentController(AppointmentService appointmentService, PetService petService, ServiceService serviceService) {
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.serviceService = serviceService;
    }

    @GetMapping("/list")
    public String listAppointments(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String serviceName,
//            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            @RequestParam(defaultValue = "0") int page,  // Trang mặc định là 0
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        Page<AppointmentDTO> appointments = appointmentService.getAllAppointment(fullName, serviceName,null,appointmentDate, PageRequest.of(page, size));
        model.addAttribute("appointments", appointments.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointments.getTotalPages());
        return "Appointments/list";
    }


    @GetMapping("/user/{userId}")
    public String getAppointmentsByUser(@PathVariable Long userId, Model model, Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentService.getByUser(userId, pageable);
        model.addAttribute("appointments", appointments);
        return "appointments/list";
    }



    @GetMapping("/{id}")
    public String getAppointmentById(@PathVariable Long id, Model model) {
        AppointmentDTO appointment = appointmentService.getById(id);
        model.addAttribute("appointment", appointment);
        return "appointments/detail";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        AppointmentDTO appointment = appointmentService.getById(id);
        List<PetDTO> petDTOList = petService.getAllPets();
        List<ServiceDTO> entities = serviceService.getAllService();
        model.addAttribute("appointment", appointment);
        model.addAttribute("pets", petDTOList);
        model.addAttribute("services", entities);
        return "Appointments/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAppointment(@PathVariable Long id, @ModelAttribute AppointmentDTO appointmentDTO) {
        appointmentDTO.setId(id);
        appointmentService.update(appointmentDTO);
        return "redirect:/superadmin/appointments/list";
    }
}
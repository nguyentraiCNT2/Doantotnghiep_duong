package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.RoomDTO;
import com.nckhntu.doantonghiep.Service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/room")
public class UserRoomController {
    private final RoomService roomService;

    public UserRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String userRoom(Model model) {
            try {
                RoomDTO roomDTO = roomService.GetRoomByMe();
                model.addAttribute("room", roomDTO);
                return "Chat";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "Chat";
            }
    }
}

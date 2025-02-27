package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.ChatDTO;
import com.nckhntu.doantonghiep.Service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/send/chat")
public class SendChatController {
    private final ChatService chatService;

    public SendChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    // Endpoint gửi tin nhắn
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute ChatDTO chatRequest) {
        try {
            chatService.sendMessage(chatRequest);
            return "redirect:/admin/room/user/"+chatRequest.getRoomId().getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

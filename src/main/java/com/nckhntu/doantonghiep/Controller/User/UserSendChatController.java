package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.ChatDTO;
import com.nckhntu.doantonghiep.Service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/send/chat")
public class UserSendChatController {
    private final ChatService chatService;

    public UserSendChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    // Endpoint gửi tin nhắn
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute ChatDTO chatRequest) {
        try {
            chatService.sendMessage(chatRequest);
            return "redirect:/user/room";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

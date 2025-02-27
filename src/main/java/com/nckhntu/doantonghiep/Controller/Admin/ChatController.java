package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.ChatDTO;
import com.nckhntu.doantonghiep.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/chat")
public class ChatController {


    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Endpoint để lấy các tin nhắn mới
    @GetMapping("/getNewMessages")
    public List<ChatDTO> getNewMessages(@RequestParam("lastId") Long lastId, @RequestParam("roomId") Long roomId) {
        // chatService.getNewMessages(lastId) trả về danh sách tin nhắn có id > lastId
        return chatService.getNewMessages(lastId, roomId);
    }


}

package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.ChatDTO;
import com.nckhntu.doantonghiep.Service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/chat")
public class UserChatController {


    private final ChatService chatService;

    public UserChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Endpoint để lấy các tin nhắn mới
    @GetMapping("/getNewMessages")
    public List<ChatDTO> getNewMessages(@RequestParam("lastId") Long lastId, @RequestParam("roomId") Long roomId) {
        // chatService.getNewMessages(lastId) trả về danh sách tin nhắn có id > lastId
        return chatService.getNewMessages(lastId, roomId);
    }


}

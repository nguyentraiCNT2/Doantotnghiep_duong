package com.nckhntu.doantonghiep.Service;



import com.nckhntu.doantonghiep.DTO.ChatDTO;

import java.util.List;

public interface ChatService {
    List<ChatDTO> getNewMessages(Long lastId, Long roomId);
    void sendMessage(ChatDTO chatRequest);
}
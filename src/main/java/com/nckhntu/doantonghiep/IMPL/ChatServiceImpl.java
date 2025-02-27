package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.ChatDTO;
import com.nckhntu.doantonghiep.Entity.ChatEntity;
import com.nckhntu.doantonghiep.Entity.RoomEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.ChatRepository;
import com.nckhntu.doantonghiep.Repository.RoomRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final HttpSession session;

    public ChatServiceImpl(ChatRepository chatRepository, RoomRepository roomRepository, UserRepository userRepository, ModelMapper modelMapper, HttpSession session) {
        this.chatRepository = chatRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.session = session;
    }

    @Override
    public List<ChatDTO> getNewMessages(Long lastId, Long roomId) {
        List<ChatEntity> newMessages = chatRepository.findByIdGreaterThanAndRoomId_Id(lastId, roomId);
        List<ChatDTO> chatDTOS = new ArrayList<>();
        newMessages.forEach(message -> {
            ChatDTO chatDTO = modelMapper.map(message, ChatDTO.class);
            chatDTOS.add(chatDTO);
        });
        return chatDTOS;
    }

    @Override
    public void sendMessage(ChatDTO chatRequest) {
        try {
            // Lấy thông tin phòng chat dựa vào roomId từ chatRequest
            RoomEntity room = roomRepository.findById(chatRequest.getRoomId().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng chat với id: " + chatRequest.getRoomId()));
            String username = (String) session.getAttribute("userEmail");
            UserEntity user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: "+username));

            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setContent(chatRequest.getContent());
            chatEntity.setRoomId(room);
            chatEntity.setUserId(user);
            chatEntity.setCreatedAt(Timestamp.from(Instant.now()));

            chatRepository.save(chatEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

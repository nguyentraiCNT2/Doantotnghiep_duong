package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.RoomDTO;
import com.nckhntu.doantonghiep.Entity.RoomEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.RoomRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomIMPL implements RoomService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HttpSession session;
    public RoomIMPL(UserRepository userRepository, RoomRepository roomRepository, ModelMapper modelMapper, HttpSession session) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
        this.session = session;
    }

    @Override
    public List<RoomDTO> GetAllRoom() {
        try {
            List<RoomEntity> roomEntities = roomRepository.findAllRoom();
            List<RoomDTO> roomDTOList = new ArrayList<>();
            roomEntities.forEach(roomEntity -> {
                roomDTOList.add(modelMapper.map(roomEntity, RoomDTO.class));

            });
            return roomDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public RoomDTO GetRoomById(Long id) {
        try {
            RoomEntity roomEntity = roomRepository.findById(id).get();
            return modelMapper.map(roomEntity, RoomDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public RoomDTO GetRoomByMe() {
        try {
            String me = (String) session.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(me).get();
            RoomEntity roomEntity = roomRepository.findByCustomer(userEntity.getId());
            return modelMapper.map(roomEntity, RoomDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

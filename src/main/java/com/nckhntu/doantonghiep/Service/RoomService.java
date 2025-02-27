package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.RoomDTO;

import java.util.List;

public interface RoomService {
    List<RoomDTO> GetAllRoom();
    RoomDTO GetRoomById(Long id);
    RoomDTO GetRoomByMe();

}

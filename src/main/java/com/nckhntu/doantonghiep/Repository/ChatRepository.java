package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long > {
    List<ChatEntity> findByIdGreaterThanAndRoomId_Id(Long lastId, Long roomId);

}

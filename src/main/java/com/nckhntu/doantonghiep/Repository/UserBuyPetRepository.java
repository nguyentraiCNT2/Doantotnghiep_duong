package com.nckhntu.doantonghiep.Repository;

import com.nckhntu.doantonghiep.Entity.UserBuyPetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBuyPetRepository extends JpaRepository<UserBuyPetEntity, Long> {
    @Query("select up from UserBuyPetEntity up where up.user.id = :userId")
    List<UserBuyPetEntity> findByUser_Id(@Param("userId") Long userId);

}

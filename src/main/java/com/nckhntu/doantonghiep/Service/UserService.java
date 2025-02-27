package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    boolean register(UserDTO user);
    UserDTO login(String email, String password);
    UserDTO me();
    void updateProfile(UserDTO user);
    void updatePassword(String oldPassword, String newPassword, String confirmPassword);
    void changeImage(MultipartFile image);
    UserDTO getById(Long id);
    Page<UserDTO> getAll(Pageable pageable);
    List<UserDTO> getAllUserDtos();
    void updateRole(Long userId, String role);
    void unActiveUser(Long userId);
    void activeUser(Long userId);
    UserDTO forgetPassword(String email);
    void resetPassword(String email, String newPassword, String confirmPassword);
}

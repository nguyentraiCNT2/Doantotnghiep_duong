package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.UserBuyPetDTO;
import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.UserBuyPetEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Repository.UserBuyPetRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.UserByPetService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserBuyPetIMPL implements UserByPetService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PetRepository petRepository;
    private final UserBuyPetRepository userBuyPetRepository;
    private final HttpSession session;

    public UserBuyPetIMPL(UserRepository userRepository, ModelMapper modelMapper, PetRepository petRepository, UserBuyPetRepository userBuyPetRepository, HttpSession session) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.petRepository = petRepository;
        this.userBuyPetRepository = userBuyPetRepository;
        this.session = session;
    }

    @Override
    public Page<UserBuyPetDTO> getAllUserBuyPet(Pageable pageable) {
        try {
            List<UserBuyPetDTO> userBuyPetDTOS = new ArrayList<>();
            Page<UserBuyPetEntity> userBuyPetEntities = userBuyPetRepository.findAll(pageable);
            userBuyPetEntities.getContent()
                    .forEach(userBuyPetEntity -> {
                        userBuyPetDTOS.add(modelMapper.map(userBuyPetEntity, UserBuyPetDTO.class));
                    });
            return new PageImpl<>(userBuyPetDTOS, pageable, userBuyPetEntities.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserBuyPetDTO> getAllUserBuyPetByUser() {
        try {
            String userName = (String) session.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userName).get();
            List<UserBuyPetDTO> userBuyPetDTOS = new ArrayList<>();
            List<UserBuyPetEntity> userBuyPetEntities = userBuyPetRepository.findByUser_Id(userEntity.getId());
            userBuyPetEntities.forEach(userBuyPetEntity -> {
                userBuyPetDTOS.add(modelMapper.map(userBuyPetEntity, UserBuyPetDTO.class));
            });
            return userBuyPetDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserBuyPetDTO> getAllUserBuyPetByUserId(Long userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId).get();
            List<UserBuyPetDTO> userBuyPetDTOS = new ArrayList<>();
            List<UserBuyPetEntity> userBuyPetEntities = userBuyPetRepository.findByUser_Id(userEntity.getId());
            userBuyPetEntities.forEach(userBuyPetEntity -> {
                userBuyPetDTOS.add(modelMapper.map(userBuyPetEntity, UserBuyPetDTO.class));
            });
            return userBuyPetDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserBuyPetDTO getUserBuyPetById(Long id) {
        try {
            UserBuyPetEntity userBuyPetEntity = userBuyPetRepository.findById(id).get();
            return modelMapper.map(userBuyPetEntity, UserBuyPetDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addPetForUser(UserBuyPetDTO userBuyPetDTO) {
        try {
            String userEmail = (String) session.getAttribute("userEmail");

// Lấy thông tin nhân viên (staff) theo email từ session, ném exception nếu không tìm thấy
            UserEntity staff = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với email: " + userEmail));

// Lấy thông tin người dùng (user) theo id từ DTO, ném exception nếu không tìm thấy
            UserEntity user = userRepository.findById(userBuyPetDTO.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + userBuyPetDTO.getUser().getId()));

// Lấy thông tin thú cưng (pet) theo id từ DTO, ném exception nếu không tìm thấy
            PetEntity pet = petRepository.findById(userBuyPetDTO.getPet().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thú cưng với id: " + userBuyPetDTO.getPet().getId()));

// Tạo mới đối tượng UserBuyPetEntity và gán các giá trị từ DTO
            UserBuyPetEntity userBuyPetEntity = new UserBuyPetEntity();
            userBuyPetEntity.setUser(user);
            userBuyPetEntity.setPet(pet);
            userBuyPetEntity.setStaff(staff);
            userBuyPetEntity.setQuantity(userBuyPetDTO.getQuantity());

// Tính tổng giá = giá của thú cưng * số lượng
            BigDecimal totalPrice = pet.getPrice().multiply(BigDecimal.valueOf(userBuyPetDTO.getQuantity()));
            userBuyPetEntity.setPrice(totalPrice);

// Gán thời gian mua là thời điểm hiện tại
            userBuyPetEntity.setBuyDate(Timestamp.from(Instant.now()));

// Lưu đối tượng vào database
            userBuyPetRepository.save(userBuyPetEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updatePetForUser(UserBuyPetDTO userBuyPetDTO) {
        try {
            UserBuyPetEntity userBuyPetEntity = userBuyPetRepository.findById(userBuyPetDTO.getId()).get();
            UserEntity userEntity = userRepository.findById(userBuyPetDTO.getUser().getId()).get();
            PetEntity pet = petRepository.findById(userBuyPetDTO.getPet().getId()).get();
            userBuyPetEntity.setQuantity(userBuyPetDTO.getQuantity());
            BigDecimal totalPrice = pet.getPrice().multiply(BigDecimal.valueOf(userBuyPetDTO.getQuantity()));
            userBuyPetEntity.setPrice(totalPrice);
            userBuyPetEntity.setPet(pet);
            userBuyPetEntity.setUser(userEntity);
            userBuyPetRepository.save(userBuyPetEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUserBuyPet(UserBuyPetDTO userBuyPetDTO) {

    }
}

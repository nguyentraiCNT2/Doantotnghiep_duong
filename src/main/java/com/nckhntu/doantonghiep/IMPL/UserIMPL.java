package com.nckhntu.doantonghiep.IMPL;

// Import các thư viện và class cần thiết

import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Entity.RoomEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.RoomRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service // Đánh dấu class này là một Service để Spring quản lý
public class UserIMPL implements UserService {
    private final UserRepository userRepository; // Thao tác với cơ sở dữ liệu User
    private final ModelMapper modelMapper; // Chuyển đổi dữ liệu giữa DTO và Entity
    private final PasswordEncoder passwordEncoder; // Mã hóa mật khẩu
    private final HttpSession httpSession; // Lưu thông tin đăng nhập của người dùng
    private final RoomRepository roomRepository;

    // Constructor để khởi tạo các thành phần cần thiết
    public UserIMPL(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, HttpSession httpSession, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
        this.roomRepository = roomRepository;
    }

    // Phương thức đăng ký tài khoản
    @Override
    public boolean register(UserDTO user) {
        try {
            // Kiểm tra xem email đã tồn tại trong hệ thống chưa
            boolean isMatchEmail = userRepository.existsByEmail(user.getEmail());
            if (isMatchEmail) {
                throw new RuntimeException("Tài khoản đã tồn tại");
            }

            // Kiểm tra mật khẩu có hợp lệ không
            if (user.getPassword().length() < 8) {
                throw new RuntimeException("Mật khẩu cần tối thiểu 8 ký tự");
            }
            if (user.getPassword().contains(" ")) {
                throw new RuntimeException("Mật khẩu không thể chứa khoảng trắng");
            }

            // Chuyển đổi từ DTO sang Entity và mã hóa mật khẩu
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setRole("USER"); // Mặc định tài khoản mới sẽ có quyền USER
            userEntity.setActive(true); // Mặc định tài khoản được kích hoạt
            userEntity.setCreatedAt(Timestamp.from(Instant.now()));

            // Lưu vào cơ sở dữ liệu
            UserEntity saveUser = userRepository.save(userEntity);
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setCustomer(saveUser);
            roomEntity.setCreatedAt(Timestamp.from(Instant.now()));
            roomEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            roomRepository.save(roomEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức đăng nhập
    @Override
    public UserDTO login(String email, String password) {
        try {
            // Tìm người dùng theo email
            UserEntity userOpt = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

            // Kiểm tra mật khẩu có khớp với mật khẩu đã lưu hay không
            boolean isMatchPassword = passwordEncoder.matches(password, userOpt.getPassword());
            if (!isMatchPassword) throw new RuntimeException("Mật khẩu không chính xác");
            if (!userOpt.getActive()) throw new RuntimeException("Tài khoản của bạn đã bị khóa");

            // Lưu thông tin vào session
            httpSession.setAttribute("userRole", userOpt.getRole());
            httpSession.setAttribute("userEmail", userOpt.getEmail());
            httpSession.setAttribute("userName", userOpt.getFullName());
            httpSession.setAttribute("userId", userOpt.getId());

            return modelMapper.map(userOpt, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức lấy thông tin tài khoản hiện tại
    @Override
    public UserDTO me() {
        try {
            String userEmail = (String) httpSession.getAttribute("userEmail");
            UserEntity user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTO.setImage(null);
            userDTO.setImageBase64(user.getImage() != null ? "/image/user/" + user.getId() : null);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức cập nhật thông tin cá nhân
    @Override
    public void updateProfile(UserDTO user) {
        try {
            String userEmail = (String) httpSession.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

            // Cập nhật thông tin
            userEntity.setAddress(user.getAddress());
            userEntity.setFullName(user.getFullName());
            userEntity.setGender(user.getGender());
            userEntity.setPhone(user.getPhone());
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức đổi mật khẩu
    @Override
    public void updatePassword(String oldPassword, String newPassword, String confirmPassword) {
        try {
            String userEmail = (String) httpSession.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

            // Kiểm tra mật khẩu cũ có đúng không
            if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
                throw new RuntimeException("Mật khẩu hiện tại không đúng");
            }

            // Kiểm tra điều kiện mật khẩu mới
            if (newPassword.length() < 8 || newPassword.contains(" ") || !newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Mật khẩu mới không hợp lệ");
            }

            // Cập nhật mật khẩu mới
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức thay đổi ảnh đại diện
    @Override
    public void changeImage(MultipartFile image) {
        try {
            String userEmail = (String) httpSession.getAttribute("userEmail");
            UserEntity userEntity = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

            // Lưu ảnh dưới dạng mảng byte
            userEntity.setImage(image.getBytes());
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Phương thức lấy thông tin người dùng theo ID (chưa được triển khai)
    @Override
    public UserDTO getById(Long id) {
        try {
            UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            return modelMapper.map(userEntity, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<UserDTO> getAll(Pageable pageable) {
        try {
            Page<UserEntity> userEntities = userRepository.findAll(pageable);
            List<UserDTO> userDTOList = new ArrayList<>();
            userEntities.getContent()
                    .stream()
                    .forEach(userEntity -> userDTOList.add(modelMapper.map(userEntity, UserDTO.class)));
            return new PageImpl<>(userDTOList, pageable, userEntities.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUserDtos() {
        try {
            List<UserEntity> userEntities = userRepository.findAll();
            List<UserDTO> userDTOList = new ArrayList<>();
            userEntities
                    .stream()
                    .forEach(userEntity -> userDTOList.add(modelMapper.map(userEntity, UserDTO.class)));
            return userDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateRole(Long userId, String role) {
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            userEntity.setRole(role);
            userRepository.save(userEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void unActiveUser(Long userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            userEntity.setActive(false);
            userRepository.save(userEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void activeUser(Long userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            userEntity.setActive(true);
            userRepository.save(userEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO forgetPassword(String email) {
        try {
            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            userDTO.setImage(null);
            return userDTO;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void resetPassword(String email, String newPassword, String confirmPassword) {
        try {

            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Mật khẩu không trùng khớp");
            }
            // Kiểm tra mật khẩu có hợp lệ không
            if (newPassword.length() < 8) {
                throw new RuntimeException("Mật khẩu cần tối thiểu 8 ký tự");
            }
            if (newPassword.contains(" ")) {
                throw new RuntimeException("Mật khẩu không thể chứa khoảng trắng");
            }
            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

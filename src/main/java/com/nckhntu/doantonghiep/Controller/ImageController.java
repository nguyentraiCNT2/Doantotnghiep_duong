package com.nckhntu.doantonghiep.Controller;

import com.nckhntu.doantonghiep.Entity.PetEntity;
import com.nckhntu.doantonghiep.Entity.ServiceEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.PetRepository;
import com.nckhntu.doantonghiep.Repository.ServiceRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@Controller
@RequestMapping("/image")
public class ImageController {
    private final ServiceRepository serviceRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public ImageController(ServiceRepository serviceRepository, PetRepository petRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<PetEntity> serviceOpt = petRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Hoặc IMAGE_JPEG tùy loại ảnh
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<byte[]> getImageService(@PathVariable Long id) {
        Optional<ServiceEntity> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isPresent() && serviceOpt.get().getImage() != null) {
            byte[] imageBytes = serviceOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Hoặc IMAGE_JPEG tùy loại ảnh
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<byte[]> getImageUser(@PathVariable Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        byte[] imageBytes;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Điều chỉnh theo loại ảnh mặc định

        if (userEntity.isPresent() && userEntity.get().getImage() != null) {
            imageBytes = userEntity.get().getImage();
        } else {
            try {
                // Thay đổi URL dưới đây thành link ảnh mặc định bạn mong muốn
                URL url = new URL("https://i.pinimg.com/736x/8f/1c/a2/8f1ca2029e2efceebd22fa05cca423d7.jpg");
                try (InputStream is = url.openStream()) {
                    imageBytes = StreamUtils.copyToByteArray(is);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}

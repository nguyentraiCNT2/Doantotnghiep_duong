package com.nckhntu.doantonghiep.Controller;

import com.nckhntu.doantonghiep.Config.CustomUserDetailsService;
import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Request.ResetPasswordRequest;
import com.nckhntu.doantonghiep.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final HttpSession httpSession;

    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserService userService, HttpSession httpSession) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password, Model model) {
        try {
        UserDTO user  = userService.login(email, password);
            if (user.getRole().equals("SUPER_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (user.getRole().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/home";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // Xử lý logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            boolean isRegistered = userService.register(userDTO);
            if (isRegistered) {
                return "redirect:/login"; // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Trả về trang đăng ký nếu có lỗi
        }
        return "register";
    }

    @GetMapping("/for-get-password")
    public String forgetPassword(){
        return "ForgetPassword";
    }
    @PostMapping("/for-get-password")
    public String forgetPassword(@RequestParam("email") String email, Model model) {
        try {
            UserDTO userDTO = userService.forgetPassword(email);
            httpSession.setAttribute("forgetPasswordEmail", userDTO.getEmail());
            return "redirect:/re-set-password";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ForgetPassword";
        }
    }
    @GetMapping("/re-set-password")
    public String reSetPassword(){
      return "ResetPassword";
    }
    @PostMapping("/re-set-password")
    public String reSetPassword(@ModelAttribute ResetPasswordRequest resetPasswordRequest, Model model) {
        try {
         userService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword());
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "ResetPassword";
        }
    }
}

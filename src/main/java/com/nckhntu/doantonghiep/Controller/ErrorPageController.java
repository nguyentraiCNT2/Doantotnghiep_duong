package com.nckhntu.doantonghiep.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {
    @GetMapping("/403")
    public String error403() {
        return "403";
    }
    @GetMapping("/404")
    public String notFound() {
        return "error-404"; // Hiển thị trang lỗi 404
    }
}

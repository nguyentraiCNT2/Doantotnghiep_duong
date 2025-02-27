package com.nckhntu.doantonghiep.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request) {
        int statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode == 404) {
            return "error-404";
        } else if (statusCode == 403) {
            return "403";
        }
        return "error-404";
    }
}

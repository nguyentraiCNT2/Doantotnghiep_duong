package com.nckhntu.doantonghiep.Config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("userRole"); // Lấy quyền từ session

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/admin") && (role == null || !(role.equals("ADMIN") || role.equals("SUPER_ADMIN")))) {
            response.sendRedirect("/403");
            return false;
        }
        if (requestURI.startsWith("/superadmin") && (role == null || !role.equals("SUPER_ADMIN"))) {
            response.sendRedirect("/403");
            return false;
        }
        if (requestURI.startsWith("/user") && (role == null || !(role.equals("USER") || role.equals("ADMIN") || role.equals("SUPER_ADMIN")))) {
            response.sendRedirect("/403");
            return false;
        }
// Nếu không tìm thấy request phù hợp → chuyển hướng đến trang 404
        if (handler == null) {
            response.sendRedirect("/404");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {}
}
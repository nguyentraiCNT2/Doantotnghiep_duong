package com.nckhntu.doantonghiep.Controller.Admin;

import com.nckhntu.doantonghiep.DTO.ReviewDTO;
import com.nckhntu.doantonghiep.Service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/comment")
public class CommentController {
    private final ReviewService reviewService;

    public CommentController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String comment(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          Model model) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ReviewDTO> reviewDTOS = reviewService.getAll(pageable);
            model.addAttribute("reviews", reviewDTOS.getContent());
            model.addAttribute("totalPages", reviewDTOS.getTotalPages());
            model.addAttribute("currentPage", page);
            return "Admin/review/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "Admin/review/list";
        }
    }


}

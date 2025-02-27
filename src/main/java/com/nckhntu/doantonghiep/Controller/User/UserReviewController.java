package com.nckhntu.doantonghiep.Controller.User;

import com.nckhntu.doantonghiep.DTO.ReviewDTO;
import com.nckhntu.doantonghiep.Service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/comment")
public class UserReviewController {
    private final ReviewService reviewService;


    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/new")
    public String newComment(@ModelAttribute ReviewDTO reviewDTO, Model model){
        try {
            reviewService.save(reviewDTO);
            return "redirect:/user/service/detail/"+reviewDTO.getServiceId().getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/user/service/detail/"+reviewDTO.getServiceId().getId();
        }
    }
}

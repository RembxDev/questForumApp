package org.it.questforumapp.controller;

import lombok.RequiredArgsConstructor;
import org.it.questforumapp.service.AnswerService;
import org.it.questforumapp.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final AnswerService answerService;

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        addUserAttributes(userDetails, model);
        addContentStatistics(model);
        return "home";
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    private void addUserAttributes(UserDetails userDetails, Model model) {
        String username = userDetails != null ? userDetails.getUsername() : "guest";
        boolean isLoggedIn = userDetails != null;

        model.addAttribute("username", username);
        model.addAttribute("isLoggedIn", isLoggedIn);
    }

    private void addContentStatistics(Model model) {
        model.addAttribute("totalPosts", postService.getTotalPostCount());
        model.addAttribute("totalAnswers", answerService.getTotalAnswerCount());
        model.addAttribute("posts", postService.getRecentPosts());
    }
}
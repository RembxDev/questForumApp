package org.it.questforumapp.controller;


import lombok.RequiredArgsConstructor;
import org.it.questforumapp.model.User;
import org.it.questforumapp.service.AnswerService;
import org.it.questforumapp.service.PostService;
import org.it.questforumapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final AnswerService answerService;

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName());
        populateProfileModel(model, user);
        return "user/profile";
    }

    @PostMapping("/admin/changeRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeUserRole(@RequestParam Integer userId, @RequestParam String newRole) {
        userService.changeUserRole(userId, newRole);
        return "redirect:/user/profile";
    }

    @PostMapping("/admin/blockUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String blockUser(@RequestParam Integer userId) {
        userService.blockUser(userId);
        return "redirect:/user/profile";
    }

    @PostMapping("/admin/unblockUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unblockUser(@RequestParam Integer userId) {
        userService.unblockUser(userId);
        return "redirect:/user/profile";
    }

    private void populateProfileModel(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getAllPostsByAuthorId(user.getId()));
        model.addAttribute("answers", answerService.getAllAnswersByUserId(user.getId()));

        boolean isAdmin = "ROLE_ADMIN".equals(user.getRole());
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            List<User> allUsers = userService.findAllUsers();
            allUsers.removeIf(u -> u.getId().equals(user.getId()));
            model.addAttribute("users", allUsers);
        }
    }
}
